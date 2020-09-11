package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchH02Parser extends HtmlProgramParser {
  
  private Properties cityCodes;
  
  public DispatchH02Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "( SELECT/UPDATE Action:SKIP! Date:DATE! Time_Out:TIME! Incident:ID! Nature:CALL! Address:ADDR! City:CITY! Cross:X! Business:PLACE! Units:UNIT! OtherUnits:UNIT/C! Notes:INFO/N+ " + 
          "| Communications%EMPTY! Dispatch%EMPTY! Incident#:ID! Report#:SKIP! Date:DATE! Time_Out:TIME! Nature:CALL! MP:CODE! Business:PLACE! Address:ADDR! City:CITY " + 
            "Addt_Address:ADD_ADDR! Cross:X! X+ Subdivision:SUBDIV! RA:CH! Neighborhood:CITY2! Notes:INFO+ Hot_Spot:INFO/N! INFO/N+ Premise:INFO/N! Units:UNIT! )");
    this.cityCodes = cityCodes;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(Dispatch|Clear|Event) Report Incident #(\\d+)");
  private static final Pattern MISSING_LINE_BRK_PTN = Pattern.compile("[ \t](?=Report#:|Time Out:|MP:|City:|RA:)");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (body.startsWith("Action:")) {
      setSelectValue("UPDATE");
      body = body.replace(" Time Out:", "\nTime Out:").replace(" City:", "\nCity:");
      return parseFields(body.split("\n"), data);
    }
    
    setSelectValue("");
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
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ADD_ADDR")) return new BaseAddAddressField();
    if (name.equals("SUBDIV")) return new BaseSubdivisionField();
    if (name.equals("CITY2")) return new BaseCity2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('\t', '/');
      super.parse(field, data);
    }
  }
  
  private static final Pattern AA_PTN = Pattern.compile("\\(S\\) *(.*?) *\\(N\\) *(.*)");
  private static final Pattern AA_BOX_PTN = Pattern.compile(".*(?<!\\bPO )\\bBOX\\b.*");
  private class BaseAddAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = AA_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        String city = match.group(2);
        if (city.length() > 0 && data.strCity.length() == 0) {
          if (cityCodes != null) city = convertCodes(city, cityCodes);
          data.strCity = city;
        }
      }
      if (AA_BOX_PTN.matcher(field).matches()) {
        data.strBox = field;
      } else {
        data.strAddress = append(data.strAddress, " ", '('+field+')');
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR BOX";
    }
  }
  
  private class BaseSubdivisionField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (AA_BOX_PTN.matcher(field).matches()) {
        data.strBox = field;
      }
      else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "BOX PLACE";
    }
    
  }
  
  private class BaseCity2Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      if (field.endsWith(" CO")) field += "UNTY";
      data.strCity = field;
    }
  }
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)? \\d\\d:\\d\\d:\\d\\d [A-Z0-9]+\\]$");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_DATE_TIME_PTN.matcher(field).replaceFirst("");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
