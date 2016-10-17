package net.anei.cadpage.parsers.CT;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CTSimsburyParser extends FieldProgramParser {
  
  public CTSimsburyParser() {
    super("SIMSBURY", "CT",
          "ADDR! Suite:APT? TYPE:CALL!");
  }

  @Override
  public String getFilter() {
    return "93001,6245,simsburyfirepage@gmail.com,4702193824";
  }
  
  private static final Pattern PREFIX_PTN = Pattern.compile("([A-Za-z ]+)(?<!TYPE):");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strSource = match.group(1).trim();
      body = body.substring(match.end()).trim();
    }
    // TODO Auto-generated method stub
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String code = p.get(' ');
      if (code.endsWith(".")) code = code.substring(0,code.length()-1);
      String call = CALL_CODES.getProperty(code);
      if (call != null) {
        data.strCode = code;
        data.strCall = call;
        data.strSupp = p.get();
      } else { 
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AA",      "Aircraft Accident",
      "BF",      "Brush/Woods Fire",
      "CS",      "Confined Space Rescue",
      "CO",      "Carbon Monoxide",
      "CONI",    "CO Detector - No Illness",
      "COWI",    "CO Detector - With Illness",
      "DO",      "Duty Officer",
      "DW",      "Down Wires",
      "FA",      "Fire Alarm",
      "HMLG",    "Haz Mat – Large",
      "HMSM",    "Haz Mat – Small",
      "LS",      "Life Star Landing",
      "MP",      "Missing Person",
      "MA",      "Mutual Aid",
      "MR",      "Mountain Rescue",
      "OG",      "Odor of Gas",
      "OP",      "Open Burning",
      "GO",      "Odor of Gas Outside",
      "GI",      "Odor of Gas Inside",
      "PA",      "Police Assist",
      "DF",      "Outside Rubbish/Dumpster Fire",
      "RF",      "Rubbish Fire",
      "SE",      "Stalled Elevator",
      "SA",      "Smoke in Area",
      "SF",      "Structure Fire",
      "VA",      "Vehicle Accident",
      "VAEX",    "Vehicle Accident – Extrication",
      "VAIN",    "Vehicle Accident – Injuries",
      "VAFL",    "Vehicle Accident – Fluids",
      "VF",      "Vehicle Fire",
      "VR",      "Vehicle Rescue",
      "WB",      "Water in Building",
      "WR",      "Water/Ice Rescue",
      "WA",      "Wires Arcing",
      "WD",      "Wires Down",
      "TR",      "Trench Rescue"
  });
}
