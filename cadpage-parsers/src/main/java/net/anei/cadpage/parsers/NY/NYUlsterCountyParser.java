package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ulster County, NY
 */
public class NYUlsterCountyParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("\\(\\(\\d+\\)[-/ A-Z0-9]*\\)");

  public NYUlsterCountyParser() {
    super("ULSTER COUNTY", "NY",
           "Unit:UNIT! UnitSts:SKIP Loc:ADDR/SXa! XSts:X! Common:PLACE Venue:CITY Inc:CALL! Date:DATE Time:TIME Addtl:INFO Nature:INFO CNTX:INFO", FLDPROG_ANY_ORDER);
  }
  
  @Override
  public String getFilter() {
    return "cad@co.ulster.ny.us,777";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = body.replace("\n", "");
    Matcher match = MARKER.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    if (!super.parseMsg(body, data)) return false;

    data.strCity = data.strCity.replaceAll(" +", " ");
    if (data.strCity.toUpperCase().startsWith("KING CITY")) data.strCity="KINGSTON";
    else if (data.strCity.equalsIgnoreCase("Out of Cty")) data.strCity = "";
    return true;
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("^\\d\\d:\\d\\d\\b");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.find()) return;
      data.strTime = match.group();
      data.strSupp = field.substring(match.end()).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "TIME INFO";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO DATE TIME";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = convertCodes(field, CALL_CODES);
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "Autoalarm",  "Automatic Alarm (Smoke Detector Activation)",
      "Crit/345",   "Medical",
      "Elec/Outdr", "Tree on Wires",
      "Haz Mat",    "Hazardous Materials Call",
      "Med Alarm",  "Lifeline activation (Medical)",
      "PDAA",       "Property Damage Auto Accident",
      "PIAA/040",   "Vehicle Accident w/injuries",
      "Public Svc", "Public Service Call",
      "Struct Fir", "Structure Fire",
      "Unkwn Fire", "Unknown Type of Fire",
      "Veh Fire",   "Vehicle Fire"
      
  });
}
