package net.anei.cadpage.parsers.WI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIRacineCountyDParser extends HtmlProgramParser {
  
  public WIRacineCountyDParser() {
    super(WIRacineCountyParser.CITY_LIST, "RACINE COUNTY", "WI", 
          "Juris:SRC! CFS:CODE_CALL! Location:ADDR/S6! Call#:ID_DATE_TIME! END");
  }
  
  @Override
  public String getFilter() {
    return "PhoenixAlert@goracine.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return false;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("CFS Notification")) return false;
    body = body.replace("</td><td>", "");
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID_DATE_TIME")) return new MyIdDateTimeField();
    return super.getField(name);
  }
  
  
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt > 0) {
        data.strCode = field.substring(0,pt);
        field = field.substring(pt+3).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?) (WI)(?: (\\d{5}))?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      field = field.replace("Town of ", "");
      super.parse(field, data);
      if (zip != null && data.strCity.length() == 0) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyIdDateTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('{');
      if (pt >= 0) {
        String dateTime = stripFieldEnd(field.substring(pt+1), "}").trim();
        field = field.substring(0,pt).trim();
        Matcher match = DATE_TIME_PTN.matcher(dateTime);
        if (!match.matches()) abort();
        data.strDate = match.group(1);
        setTime(TIME_FMT, match.group(2), data);
      }
      data.strCallId = field;
    }

    @Override
    public String getFieldNames() {
      return "ID DATE TIME";
    }
  }
}
