package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LATerrebonneParishBParser extends FieldProgramParser {

  public LATerrebonneParishBParser() {
    super("TERREBONNE PARISH", "LA", 
          "ADDR/SL CITY ST_X! END");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(LATerrebonneParishAParser.MWORD_STREET_LIST);
    setupProtectedNames("J AND V GUIDRY");
  }
  
  @Override
  public String getFilter() {
    return "Zuercher@tpe911.com,tpe911@tpe911.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, " [Attachment(s) removed]");
    body = stripFieldEnd(body, " None");
    return parseFields(body.split(","), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ST_X")) return new MyStateCrossField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_ADDR_PTN = Pattern.compile("(\\d+[A-Z]?) +(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE " + super.getFieldNames();
    }
  }
  
  private static final Pattern ST_ZIP_X_PTN = Pattern.compile("([A-Z]{2}) \\d{5}\\b(?: +(.*))?");
  private class MyStateCrossField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ST_ZIP_X_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strState = match.group(1);
      data.strCross = getOptGroup(match.group(2));
    }
    
    @Override
    public String getFieldNames() {
      return "ST X";
    }
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT PEDESTRIAN",
      "ACCIDENT W/INJURY",
      "ACCIDENT W/O INJURY",
      "ALARM - CARBON MONOXIDE",
      "ALARM - FIRE",
      "ARCING INSIDE",
      "ARCING OUTSIDE",
      "BUILDING OR STRUCTURE WEAKEND OR COLLASPED",
      "EXTRACATION RESCUE",
      "FIRE - DUMPSTER",
      "FIRE - GRASS",
      "FIRE - MOBILE PROPERTY",
      "FIRE - OUTSIDE RUBBISH",
      "FIRE - STRUCTURE",
      "HAZARDOUS CONDITION",
      "MEDICAL ASSIST",
      "MEDICAL EMERGENCY",
      "POWER LINE DOWN",
      "SERVICE CALL",
      "SMOKE ODOR INSIDE",
      "SMOKE ODOR OUTSIDE",
      "XFER TPSO"
 );
}
