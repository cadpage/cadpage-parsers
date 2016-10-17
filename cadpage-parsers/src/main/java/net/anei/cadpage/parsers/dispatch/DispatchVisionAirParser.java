package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchVisionAirParser extends FieldProgramParser {
  
  private String[] prefixs;
  
  /**
   * @deprecated - Switch to DispatchA3 when convenient 
   */
  public DispatchVisionAirParser(String prefix, String defCity, String defState, String program) {
    this(new String[]{prefix}, defCity, defState, program);
  }
  
  
  /**
   * @deprecated - Switch to DispatchA3 when convenient 
   */
  public DispatchVisionAirParser(String[] prefixs, String defCity, String defState, String program) {
    super(defCity, defState, program);
    this.prefixs = prefixs;
  }
  
  private static final Pattern DELIM = Pattern.compile("(?<!\\*)\\*[ \n]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    boolean found = false;
    for (String prefix : prefixs) {
      if (body.startsWith(prefix)) {
        body = body.substring(prefix.length()).trim();
        found = true;
        break;
      }
    }
    if (!found) return false;
    if (body.endsWith("*")) body = body + " ";
    return parseFields(DELIM.split(body), data);
  }
  
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      if (field.startsWith("0 ")) field = field.substring(2).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern START_STAR_PTN = Pattern.compile("^\\*+");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = START_STAR_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
  
  private static final Pattern LINE_PREFIX = Pattern.compile("^Line\\d+=");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = LINE_PREFIX.matcher(field);
      if (match.find()) field = field.substring(match.end()).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern EXTRA_MARKER = Pattern.compile("\\b(\\d?\\d/\\d?\\d/\\d{4}) (\\d?\\d:\\d?\\d:\\d?\\d(?: [AP]M)?) : (pos\\d+) : [A-Za-z0-9]+\\b");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern EXTRA_DELIM = Pattern.compile("\\*\\* EMD (?:Case Entry Finished|Case Complete|Recommended Dispatch) \\*\\*|\\bResponse Text:|\\bKey Questions:|\\bGeo Comment:|\\bLandmark Comment:|Narrative ?:|\\b(?=Cross Streets:|Landmark:|NBH:)|  +", Pattern.CASE_INSENSITIVE);
  private static final String TRUNC_EXTRA_PATTERN = "NN/NN/NNNN NN:NN:NN : posN :";
  private class BaseExtraField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Line18=")) field = field.substring(7).trim();
      Matcher match = EXTRA_MARKER.matcher(field);
      if (!match.find()) {
        if (field.length() < 3) return false;
        if (!TRUNC_EXTRA_PATTERN.startsWith(field.replaceAll("\\d", "N"))) return false;
        if (field.length() >= 10) data.strDate = field.substring(0,10);
        if (field.length() >= 19) data.strTime = field.substring(11,19);
        if (field.length() >= 26) data.strChannel = field.substring(22,26);
        return true;
      }
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      data.strChannel = match.group(3);
      field = field.substring(match.end()).trim();
      
      for (String fld1 : EXTRA_MARKER.split(field)) {
        String connect = "\n";
        for (String fld2 : EXTRA_DELIM.split(fld1)) {
          fld2 = fld2.trim();
          if (fld2.length() == 0) continue;
          
          String upshift = fld2.toUpperCase();
          if (upshift.startsWith("LANDMARK:")) {
            if (data.strPlace.length() == 0) data.strPlace = fld2.substring(9).trim();
            continue;
          }
          
          if (upshift.startsWith("CROSS STREETS:")) {
            fld2 = fld2.substring(14).trim();
            String saveCross = data.strCross;
            int pt = fld2.indexOf("//");
            if (pt < 0) {
              data.strCross = fld2;
              fld2 = "";
            } else {
              String prefix = fld2.substring(0,pt).trim();
              fld2 = fld2.substring(pt+2);
              if (fld2.startsWith(" ")) {
                fld2 = fld2.trim();
              } else {
                Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, fld2);
                if (res.isValid()) {
                  res.getData(data);
                  fld2 = res.getLeft();
                } else {
                  data.strCross = fld2;
                  fld2 = "";
                }
              }
              data.strCross = append(prefix, " / ", data.strCross);
            }
            if (saveCross.length() > 0) data.strCross = saveCross;
          }
          
          if (fld2.length() > 0 && !fld2.equals(":") && !data.strSupp.contains(fld2)) {
            data.strSupp = append(data.strSupp, connect, fld2);
            connect = " / ";
          }
        }
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (checkParse(field, data)) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME CH X PLACE INFO";
    }
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("EXTRA")) return new BaseExtraField();
    return super.getField(name);
  }
  
  
}
