
package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jefferson County, AL
 */
public class ALJeffersonCountyLParser extends DispatchH05Parser {

  public ALJeffersonCountyLParser() {
    super("JEFFERSON COUNTY", "AL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITYST! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "@HOMEWOODAL.ORG,noreply@homewoodal.net";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.*?), *(.*\\d.*|[A-Z]|)");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = TRAIL_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
}
