package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNAitkinCountyParser extends FieldProgramParser {

  public MNAitkinCountyParser() {
    super("AITKIN COUNTY", "MN",
          "CALL ADDR1+? ADDRCITYST MAP MAP/L! INFO/CS+? DATETIME PHONE ID END");
  }

  @Override
  public String getFilter() {
    return "donotreply@co.aitkin.mn.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Respond to Incident")) return false;
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(body.split(","), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddressCityStateField(false);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField(true);
    if (name.equals("MAP")) return new MapField("(?:Township|Section) - +(?:None()|(.*))", true);
    if (name.equals("DATETIME")) return new DateTimeField("Time of Call - +(\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d)", true);
    if (name.equals("PHONE")) return new PhoneField("Caller phone - +(?:None()|(.*))", true);
    if (name.equals("ID")) return new IdField("Call Number - +(\\d+)", true);
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {

    private final boolean complete;

    public MyAddressCityStateField(boolean complete) {
      this.complete = complete;
    }

    @Override
    public boolean canFail() {
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, ", ", field);
      if (complete) {
        String addr = data.strAddress;
        data.strAddress = "";
        super.parse(addr, data);
      }
    }
  }
}
