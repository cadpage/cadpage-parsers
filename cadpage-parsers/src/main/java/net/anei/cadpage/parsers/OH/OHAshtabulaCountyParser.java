package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA10Parser;


public class OHAshtabulaCountyParser extends DispatchA10Parser {

  public OHAshtabulaCountyParser() {
    super(CITY_LIST, "ASHTABULA COUNTY", "OH",
           "CALL ( ADDR/Z CITY/Z ST | ADDR/Z CITY ST? | ADDR/S ) X! X+? INFO+");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  @Override
  public String getFilter() {
    return "@ashtabulacounty.us,@countycad.us,777";
  }

  private static final Pattern SRC_PTN = Pattern.compile("([ A-Z]+): +(.*)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    String subCall = null;
    Matcher match = SRC_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1).trim().replace(' ', '_');
      subCall = match.group(2);
    } else if ((match = SRC_PTN.matcher(body)).matches()) {
      data.strSource = match.group(1).trim().replace(' ', '_');;
      body = match.group(2);
    }

    if (!super.parseMsg(body, data)) return false;

    if (subCall != null) {
      data.strCode = data.strCall;
      data.strCall = subCall;
    }

    data.strCity = stripFieldEnd(data.strCity, " TWP");
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC CODE " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("\\d{2}", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ST")) return new StateField("(OH|PA)(?: +\\d{5})?|()[ A-Z]+ CO", true);
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) field = "ALERT";
      super.parse(field, data);
    }
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*) +(OH|PA)(?: +\\d{5})?");
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (parseCityState(field, data)) return true;
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      if (parseCityState(field, data)) return;
      super.parse(field, data);
    }

    private boolean parseCityState(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1).trim(), data);
      data.strState = match.group(2);
      return true;
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }


  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ASHTABULA",
    "CONNEAUT",
    "GENEVA",

    // Vilages
    "ANDOVER",
    "GENEVA ON THE LAKE",
    "JEFFERSON",
    "NORTH KINGSVILLE",
    "ORWELL",
    "ROAMING SHORES",
    "ROCK CREEK",

    // Townships
    "ANDOVER TWP",
    "ASHTABULA TWP",
    "AUSTINBURG TWP",
    "CHERRY VALLEY TWP",
    "COLEBROOK TWP",
    "DENMARK TWP",
    "DORSET TWP",
    "GENEVA TWP",
    "HARPERSFIELD TWP",
    "HARTSGROVE TWP",
    "JEFFERSON TWP",
    "KINGSVILLE TWP",
    "LENOX TWP",
    "MONROE TWP",
    "MORGAN TWP",
    "NEW LYME TWP",
    "ORWELL TWP",
    "PIERPONT TWP",
    "PLYMOUTH TWP",
    "RICHMOND TWP",
    "ROME TWP",
    "SAYBROOK TWP",
    "SHEFFIELD TWP",
    "TRUMBULL TWP",
    "WAYNE TWP",
    "WILLIAMSFIELD TWP",
    "WINDSOR TWP",

    // Townships with suffix
    "ANDOVER",
    "ASHTABULA",
    "AUSTINBURG",
    "CHERRY VALLEY",
    "COLEBROOK",
    "DENMARK",
    "DORSET",
    "GENEVA",
    "HARPERSFIELD",
    "HARTSGROVE",
    "JEFFERSON",
    "KINGSVILLE",
    "LENOX",
    "MONROE",
    "MORGAN",
    "NEW LYME",
    "ORWELL",
    "PIERPONT",
    "PLYMOUTH",
    "RICHMOND",
    "ROME",
    "SAYBROOK",
    "SHEFFIELD",
    "TRUMBULL",
    "WAYNE",
    "WILLIAMSFIELD",
    "WINDSOR",

    // Census designated places
    "EDGEWOOD",

    // Other communities
    "AUSTINBURG",
    "DORSET",
    "EAGLEVILLE",
    "FOOTVILLE",
    "KINGSVILLE",
    "PIERPONT",
    "UNIONVILLE",

    // Geauga County
    "HUNTSBURG",
    "MIDDLEFIELD",
    "MONTVILLE",
    "THOMPSON",

    // Trumbull County
    "BLOOMFIELD"
  };
}
