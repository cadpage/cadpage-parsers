package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA12Parser extends FieldProgramParser {
  
  public DispatchA12Parser(String defCity, String defState) {
    super(defCity, defState,
          "CALL! Loc:ADDR Rcvd:TIME! Units:UNIT? Comments:INFO");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("Times -")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("UNIT")) return new BaseUnitField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_PTN = Pattern.compile("(.*) ([^ ]+) \\((.*)\\)");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strSource = match.group(2).trim();
      data.strCode = match.group(3).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "CALL SRC CODE";
    }
  }
  
  private static final Pattern ADDR_DELIM_PTN = Pattern.compile("[;\\[]#|[;\\[]@|[:#;]");
  private static final Pattern ADDR_APT_BLK_PTN = Pattern.compile(".*\\bBLK", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT_PREFIX_PTN = Pattern.compile("^(?:APT|RM|ROOM|SUITE|STE) *(.*)|(?:LOT.*)|.{1,4}", Pattern.CASE_INSENSITIVE);
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String lastDelim = null;
      int lastCol = 0;
      Matcher match = ADDR_DELIM_PTN.matcher(field);
      while (match.find()) {
        parseAddressPart(lastDelim, field.substring(lastCol, match.start()).trim(), data);
        lastDelim = match.group();
        lastCol = match.end();
      }
      parseAddressPart(lastDelim, field.substring(lastCol).trim(), data);
    }
    
    private void parseAddressPart(String type, String part, Data data) {
      if (type == null) {
        int pt = part.lastIndexOf(',');
        if (pt >= 0) {
          data.strCity = part.substring(pt+1).trim();
          part = part.substring(0,pt).trim();
        }
        super.parse(part, data);
      }
      
      else if (type.equals(":")  || type.equals("[#") || type.equals("#")) {
        data.strApt = append(data.strApt, "-", part);
      }
      
      else if (type.equals("[@") || type.equals(";@")) {
        data.strPlace = append(data.strPlace, " - ", part);
      }
      
      else if (type.equals(";")) {
        if (ADDR_APT_BLK_PTN.matcher(part).matches()) {
          data.strAddress = append(part, " ", data.strAddress);
        }
        else { 
          Matcher match = ADDR_APT_PREFIX_PTN.matcher(part);
          if (match.matches()) {
            if (match.group(1) != null) part = match.group(1);
            data.strApt = append(data.strApt, "-", part);
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
      
      else abort();  // Can't happen unless we screwed up somewhere
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCallId = p.getLast(' ');
      data.strUnit = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }
  }
}
