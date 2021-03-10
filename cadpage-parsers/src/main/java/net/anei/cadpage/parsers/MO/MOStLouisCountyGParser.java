package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOStLouisCountyGParser extends DispatchGlobalDispatchParser {


  public MOStLouisCountyGParser() {
    super(MOStLouisCountyParser.CITY_LIST, "ST LOUIS COUNTY", "MO", LEAD_SRC_UNIT_ADDR,
          Pattern.compile("FD|STILL"), Pattern.compile("[A-Z]{0,2}\\d{4}|\\d{2}[A-Z]{0,3}|DUTY"));
    setupCallList(CALL_LIST);

  }

  @Override
  public String getFilter() {
    return "Kirkwood@Kirkwoodmo.org";
  }

  private static final Pattern TAC_PTN = Pattern.compile(" *\\b((?:DUTY )?TAC \\S+)\\b *");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active 911 Paging")) return false;
    Matcher match = TAC_PTN.matcher(body);
    if (match.find()) {
      data.strChannel = match.group(1);
      body = append(body.substring(0,match.start()), " ", body.substring(match.end()));
    }
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.equals("-")) data.strApt = "";
    return true;
  }

  @Override
  public String getProgram() {
    return "CH " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  protected class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " MO");
      super.parse(field, data);
    }
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "1ST ALARM/ FIRE COMMERCIAL",
      "1ST ALARM/ FIRE RESIDENTIAL",
      "ASSIST PATIENT",
      "ASSIST POLICE",
      "BASIC EMS",
      "BRUSH FIRE",
      "BRUSH FIRE STILL",
      "BRUSH WITH EXPOSURES",
      "CAR FIRE WITH EXPOSURES",
      "CHECK THE AREA",
      "CHECK THE AREA STILL",
      "CHECK THE BUILDING STILL",
      "CO DETECTOR",
      "COMPLAINT",
      "ELEVATOR RESCUE",
      "EXTERIOR GAS LEAK",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENTIAL",
      "FIRE INSPECTION",
      "FLUE FIRE",
      "GAS LEAK",
      "GAS LEAK COMMERCIAL",
      "GAS LEAK RESIDENTIAL",
      "HAZ-MAT INCIDENT",
      "LIFE THREAT EMS",
      "LIGHTNING STRIKE",
      "LOCK OUT",
      "MCI",
      "MOVE UP",
      "MUTUAL AID",
      "MVA WITH INJURIES",
      "MVA WITH RESCUE",
      "NON MVA RESCUE RESPONSE",
      "RESIDENTIAL LOCK OUT",
      "SMELL OF ELECTRIC COMMERCIAL",
      "SMELL OF ELECTRIC RESIDENTIAL",
      "SMELL OF SMOKE COMMERCIAL",
      "SMELL OF SMOKE RESIDENTIAL",
      "SMOKE IN BUILDING RESIDENTIAL",
      "SMOKE IN THE BUILDING COMMERCIAL",
      "STILL ASSIST POLICE",
      "STILL BRUSH FIRE",
      "STILL CHECK THE AREA",
      "STILL CHECK THE BUILDING",
      "STILL CO DETECTOR WITH ILLNESS",
      "STILL CO DETECTOR",
      "STILL DUMPSTER FIRE NO EXPOSURE",
      "STILL DUMPSTER WITH EXPOSURE",
      "STILL ELEVATOR RESCUE",
      "STILL ELEVATOR STALLED",
      "STILL EXTERIOR GAS LEAK",
      "STILL FUEL SPILL",
      "STILL RESIDENTIAL LOCK OUT",
      "STILL TRANSFORMER FIRE",
      "STILL VEHICLE FIRE COMMERCIAL",
      "STILL VEHICLE FIRE NON COMMERCIAL",
      "STILL VEHICLE LOCKOUT",
      "STILL WASH DOWN",
      "STILL WIRES DOWN",
      "TECHNICAL RESCUE",
      "TRACTOR TRAILER FIRE",
      "TRANSFORMER FIRE",
      "UPGRADED LIFE RESPONSE",
      "VEHICLE FIRE NON COMMERCIAL STILL",
      "VEHICLE FIRE WITH EXPOSURES",
      "VEHICLE LOCKOUT",
      "WASH DOWN",
      "WIRES DOWN"
      );

}
