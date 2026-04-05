package net.anei.cadpage.parsers.CA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.FieldProgramParser;

/**
 * Marin County, CA
 */
public class CAMarinCountyParser extends FieldProgramParser {

  public CAMarinCountyParser() {
    super(CITY_CODES, "MARIN COUNTY", "CA",
          "Location:ADDR/S? Beat:MAP! TYPE_CODE:CALL! Alarm_Lev:PRI! Typecode:SKIP! Time:TIME! Dgroup:CH! MapGrid:GRID/L! MapPage:MAP/L! END");
  }

  @Override
  public String getFilter() {
    return "CAD@marinsheriff.org";
  }

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("Original message from terminal .*? (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) *.*? \\(\\d+\\): *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD PAGE")) return false;

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("DATE TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      data.strSupp = match.group(3);
      return true;
    }

    else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GRID")) return new MapField("Grid (.*)|()", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(": @");
      String apt = p.getLastOptional(',');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }

  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "22",     "Vehicle Accident / Extrication Assignment",
      "29",     "Vehicle Accident / Extrication Assignment",
      "BF",     "Boat Fire",
      "EXPL",   "Explosion",
      "HBF",    "Houseboat Fire",
      "MED",    "Medical",
      "NATGAS", "Natural Gas Leak",
      "PLANE",  "Plane Crash",
      "ROPE",   "Rope Rescue",
      "SC",     "Structure Collapse",
      "SF",     "Structure Fire",
      "SMOKEB", "Smoke in a Building",
      "VEG",    "Vegetation Fire",
      "VFB",    "Vehicle Fire near a Building",
      "VFC",    "Vehicle Fire (Commercial)",
      "VFF",    "Vehicle Fire (Freeway)",
      "WATER",  "Water Rescue"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GGN", "GOLDEN GATE NATIONAL RECREATION AREA",
      "MVP", "MILL VALLEY",
      "SAS", "SAUSALITO",
      "SBY", "STRAWBERRY",
      "TAM", "TAMALPAIS-HOMESTEAD VALLEY"
  });
}
