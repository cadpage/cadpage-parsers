package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCPittCountyBParser extends DispatchOSSIParser {

  public NCPittCountyBParser() {
    super(CITY_CODES, "PITT COUNTY", "NC",
          "( CANCEL ( ADDRCITY! | ADDR CITY! ) " +
          "| UNIT/Z STATUS/R ADDR CITY CALL/SDS! END " +
          "| CALL PLACE? ( ADDRCITY ( DATETIME! | IDQ ( EMPTY/Z PRI | PRIQ EMPTY? ) DATETIME! ) " +
                        "| ADDR/Z PLACE ID/Z CITY DATE TIME! " +
                        "| ADDR/Z CITYQ ( DATETIME! | IDQ PRIQ? DATETIME! ) " +
                        "| ADDR/Z ID CITYQ? PRIQ? DATETIME! " +
                        "| ADDR/Z PRI DATETIME! " +
                        "| ADDR/Z DATETIME! " +
                        ") EMPTY+? BOX? ( SRC UNIT? | UNIT | PLACE SRC? UNIT? ) EMPTY+? CH+? EMPTY+? X+? " +
          ") INFO/N+");
    setupCities("BEAUFORT CO");
    setupMultiWordStreets("MARTIN LUTHER KING JR", "STOKESTOWN ST JOHNS");
    addRoadSuffixTerms("ALTERNATE", "ROADWAY");
  }

  @Override
  public String getFilter() {
    return "CAD@pittcountync.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Text Message")) return false;

    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();

    body = stripFieldStart(body, "_");

    body = body.replace('\ufffd', ' ');

    if (body.contains(",Enroute,")) {
      return parseFields(body.split(","), data);
    }

    body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.startsWith("OUTSIDE COUNTY")) data.defCity = "";
    return true;
  }

  @Override
  public String getProgram() {
    return "UNIT? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new MyCancelField();
    if (name.equals("STATUS")) return new CallField("Enroute", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("\\d{11}", true);
    if (name.equals("IDQ")) return new IdField("\\d{11}|", true);
    if (name.equals("CITYQ")) return new MyCityField();
    if (name.equals("PRI")) return new PriorityField("[P1-9]", true);
    if (name.equals("PRIQ")) return new PriorityField("[P1-9]|", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("BOX")) return new BoxField("\\d\\d", true);
    if (name.equals("SRC")) return new SourceField("(?:F|R|STA)\\d+", true);
    if (name.equals("UNIT")) return new UnitField("(?:[A-Z]+\\d+[A-Z]?||\\d{4}|FS)(?:,.*)?", true);
    if (name.equals("CH")) return new ChannelField("(TAC.*|A\\d{1,2})|Radio Channel: *(.*)");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCancelField extends BaseCancelField {
    public MyCancelField() {
      super("County Working Incident|ELECTRIC UTILITIES|GAS WATER ELECTRIC|INVESTIGATOR NOTIFIED|MEDICAL EXAMINER|NCDOT NOTIFIED|PATIENT EXTRICATED|RED CROSS|STAGING IN THE AREA|UTILITY GAS|UTILITY WATER|EASTCARE (?:DISPATCHED|CANCELLED|STANDBY)|[A-Z]+ WORKING INCIDENT");
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("CONFIRMED PIN IN")) return false;
      return super.checkParse(field, data);
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("LL:")) return false;
      return super.checkParse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }
  }

  private static final Pattern INFO_CH_PTN = Pattern.compile("TAC.*|A\\d{1,2}");
  private static final Pattern INFO_CODE_PTN = Pattern.compile("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile("\\d{10}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
        return;
      }

      if (INFO_CH_PTN.matcher(field).matches()) {
        data.strChannel = field;
        return;
      }

      if (field.equalsIgnoreCase("BEAUFORT COUNTY")) {
        if (data.strCity.length() == 0) {
          data.strCity = field;
          return;
        }
      }

      if (data.strCode.length() == 0 && INFO_CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
        return;
      }

      if (isLastField() && TRAIL_ID_PTN.matcher(field).matches()) {
        data.strCallId =  append(data.strCallId, "/", field);
        return;
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH CITY CODE " + super.getFieldNames() + " ID";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AYDE", "AYDEN",
      "BELL", "BELL ARTHUR",
      "BEAU", "BEAUFORT COUNTY",
      "BETH", "BETHEL",
      "CHOC", "CHOCOWINITY",
      "FALK", "FALKLAND",
      "FARM", "FARMVILLE",
      "FOUN", "FOUNTAIN",
      "GREE", "GREENVILLE",
      "GRIF", "GRIFTON",
      "GRIM", "GRIMESLAND",
      "KINS", "KINSTON",
      "MACC", "MACCELSFIELD",
      "ROBE", "ROBERSONVILLE",
      "SIMP", "SIMPSON",
      "SNHL", "SNOW HILL",
      "STOK", "STOKES",
      "TARB", "TARBORO",
      "VANC", "VANCEBORO",
      "WALS", "WALSTONBURG",
      "WASH", "WASHINGTON",
      "WINT", "WINTERVILLE"
  });
}
