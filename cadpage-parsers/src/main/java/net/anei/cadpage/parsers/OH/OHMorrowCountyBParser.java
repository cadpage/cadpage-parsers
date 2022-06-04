package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Morrow County, OH
 */
public class OHMorrowCountyBParser extends FieldProgramParser {

  public OHMorrowCountyBParser() {
    super("MORROW COUNTY", "OH",
          "LOCATION:ADDRCITYST! GPS X INFO CH! END");
    setupGpsLookupTable(OHMorrowCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "911text@mcems.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) return false;
    data.strCall = subject;

    if (!parseFields(body.split("\\|"), data)) return false;

    OHMorrowCountyParser.fixCity(data);
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CH")) return new MyChannelField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return OHMorrowCountyParser.doAdjustMapAddress(sAddress);
  }
}
