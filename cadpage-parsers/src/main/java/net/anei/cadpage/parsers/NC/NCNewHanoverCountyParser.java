package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCNewHanoverCountyParser extends DispatchOSSIParser {
  
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("^=[=A-Z]*(?=CAD:)");
  
  public NCNewHanoverCountyParser() {
    super(CITY_CODES, "NEW HANOVER COUNTY", "NC",
          "FYI? ( CITY ADDR CALL SRC! | CH2 ADDR CITY/Y! EXTRA2 | SRC? CALL ADDR! EXTRA ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = LEAD_JUNK_PTN.matcher(body).replaceFirst("");
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CH2")) return new MyChannel2Field();
    if (name.equals("EXTRA2")) return new MyExtra2Field();
    if (name.equals("SRC")) return new SourceField("ST\\d+|W\\d+|[A-Z]{1,2}FD", true);
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CH2_PTN = Pattern.compile("PUBLIC SAFETY CHANNEL (\\d+)");
  private class MyChannel2Field extends ChannelField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CH2_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strChannel = "PS" + match.group(1);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern EXTRA2_CH_PTN = Pattern.compile("\\**\\b(PS ?\\d+)\\b\\**");
  private static final Pattern EXTRA2_APT_PTN = Pattern.compile("(?:APT|LOT|RM|TRLR) +(.*)|(\\d{1,4})");
  private static final Pattern EXTRA2_ID_PTN = Pattern.compile("\\d{6,}");
  private class MyExtra2Field extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = EXTRA2_CH_PTN.matcher(field);
      if (match.find()) {
        data.strChannel = match.group(1).replace(" ", "");
        field = append(field.substring(0,match.start()).trim(), " ", field.substring(match.end()).trim());
      }
      
      match = EXTRA2_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = match.group(2);
        data.strApt = append(data.strApt, "-", apt);
      } else if (EXTRA2_ID_PTN.matcher(field).matches()) {
        data.strCallId = field;
      } else {
        data.strSupp = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO APT ID CH";
    }
  }
  
  private static final Pattern ASTERISK_PTN = Pattern.compile("\\*\\*+");
  private static final Pattern CHANNEL_PTN = Pattern.compile("\\*(TAC\\d+)\\*", Pattern.CASE_INSENSITIVE);
  private static final Pattern ID_PTN = Pattern.compile("\\d{5,}");
  private static final Pattern PLACE_PTN = Pattern.compile("[A-Z ]+");
  private class MyExtraField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = ASTERISK_PTN.matcher(field).replaceAll("*");
      Matcher match = CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1).toUpperCase();
        return;
      }
      
      match = ID_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = field;
        return;
      }
      
      match = PLACE_PTN.matcher(field);
      if (match.matches()) {
        String city = CITY_CODES.getProperty(field);
        if (city != null) {
          data.strCity = city;
        } else {
          data.strPlace = field;
        }
        return;
      }
      
      data.strSupp = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CH ID CITY PLACE INFO";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = ASTERISK_PTN.matcher(field).replaceAll("*");
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CB",   "CAROLINA BEACH",
      "CH",   "CASTLE HAYNE",
      "KB",   "KURE BEACH",
      "WB",   "WRIGHTSVILLE BEACH", 
      "WM",   "WILMINGTON"
  });
}
