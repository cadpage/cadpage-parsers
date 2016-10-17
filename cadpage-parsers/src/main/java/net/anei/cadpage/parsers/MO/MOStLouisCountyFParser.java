package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStLouisCountyFParser extends FieldProgramParser {

  public MOStLouisCountyFParser() {
    super("ST LOUIS COUNTY", "MO", "ADDR/SC! Description:INFO CrossStreets:X");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BRENTWOOD PROMENADE",
        "HIGH HAMPTON",
        "LOG CABIN",
        "LOUIS GALLERIA",
        "STONELEIGH TOWERS",
        "THE BOULEVARD SAINT LOUIS",
        "TWIN SPRINGS"
    );
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static Pattern UNIT_FD_CALLADDR = Pattern.compile("(.*?) FD (.*?)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = UNIT_FD_CALLADDR.matcher(field);
      if (!mat.matches()) abort();
      data.strUnit = mat.group(1).trim();
      super.parse(mat.group(2).trim(), data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL ADDR APT";
    }
  }
  
  private static CodeSet CALL_LIST = new CodeSet(new String[]{
    "ACCIDENTAL INJURY",
    "AUTO ACCIDENT WITH INJURIES",
    "CARBON MONOXIDE ALARM SOUNDING",
    "COMMERCIAL FIRE ALARM",
    "EMS MUTUAL AID",
    "FIRE MUTUAL AID",
    "INVALID ASSIST",
    "MISC STILL ALARM",
    "RESIDENTIAL FIRE ALARM",
    "RESIDENTIAL STRUCTURE FIRE",
    "SICKCASE",
    "SMELL OF GAS INSIDE",
    "SMELL OF GAS OUTSIDE",
    "SMELL OF SMOKE OUTSIDE",
    "SMELL OF SMOKE/ELECTRICAL INSIDE",
    "SMOKE IN THE BUILDING",
    "WATER PROBLEM",
    "WIRES DOWN/TRANSFORMER"
  });
  
}
