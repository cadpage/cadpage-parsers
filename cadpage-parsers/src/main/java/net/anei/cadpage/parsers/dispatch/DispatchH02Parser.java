package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchH02Parser extends HtmlProgramParser {
  
  public DispatchH02Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, 
          "Communications%EMPTY! Dispatch%EMPTY! Incident#:ID! Report#:SKIP! Date:DATE! Time_Out:TIME! Nature:CALL! MP:CODE! Business:PLACE! Address:ADDR! City:CITY Addt_Address:ADD_ADDR! Cross:X! X+ Subdivision:EMPTY! RA:CH! Neighborhood:CITY2! Notes:INFO+ Hot_Spot:INFO/N! INFO/N+ Premise:INFO/N! Units:UNIT!");
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(Dispatch|Clear|Event) Report Incident #(\\d+)");
  private static final Pattern MISSING_LINE_BRK_PTN = Pattern.compile("[ \t](?=Report#:|Time Out:|MP:|City:|RA:)");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    if (!match.group(1).equals("Dispatch")) {
      setFieldList("ID");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(2);
      return true;
    }
    
    body = MISSING_LINE_BRK_PTN.matcher(body).replaceAll("\n");
    if (body.startsWith("<html>")) return super.parseHtmlMsg(subject, body, data);
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADD_ADDR")) return new MyAddAddressField();
    if (name.equals("CITY2")) return new MyCity2Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strAddress = append(data.strAddress, " ", '('+field+')');
    }
  }
  
  private class MyCity2Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      if (field.endsWith(" CO")) field += "UNTY";
      data.strCity = field;
    }
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)? \\d\\d:\\d\\d:\\d\\d [A-Z0-9]+\\]$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_DATE_TIME_PTN.matcher(field).replaceFirst("");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
