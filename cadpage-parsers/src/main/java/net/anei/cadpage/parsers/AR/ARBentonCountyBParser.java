package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARBentonCountyBParser extends FieldProgramParser {

  public ARBentonCountyBParser() {
    super("BENTON COUNTY", "AR",
          "INC_TYPE:CALL! COMPANIES:UNIT! ADDRESS:ADDR!");
  }

  @Override
  public int getMapFlags() {
    return MsgInfo.MAP_FLG_SUPPR_AND_ADJ;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Dispatch")) return false;
    if (!body.startsWith("***")) return false;
    body = body.substring(3).trim();
    if (body.endsWith("***EOT***")) {
      body = body.substring(0,body.length()-9).trim();
    } else {
      data.expectMore = true;
    }
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PTN = Pattern.compile("([^,]*) , ([ A-Z]*) , ([A-Z]{2})(?: ([^ ].*?))?  (.*?)(?:\\bMap (\\d{4}))?");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith(",")) field = ' ' + field;
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      String addr = match.group(1).trim();
      data.strCity = match.group(2).trim();
      String state = match.group(3);
      if (!state.equals("AR")) data.strState = state;
      data.strPlace = getOptGroup(match.group(4));
      data.strSupp = match.group(5).trim();
      data.strMap = getOptGroup(match.group(6));

      if (addr.length() == 0) {
        addr = data.strPlace;
        data.strPlace = "";
      }
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, addr, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST PLACE INFO MAP";
    }
  }
}
