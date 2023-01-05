package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Christian County, KY
 */
public class KYChristianCountyParser extends DispatchA27Parser {

  public KYChristianCountyParser() {
    super("CHRISTIAN COUNTY", "KY", "[A-Z0-9]+|Jsmc|Fhfd");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,noreply@hopkinsvilleky.us,noreply@cis.com,noreply@h-ky.us";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern POUND_DIR_PTN = Pattern.compile("  *#([NSEW]B)\\b", Pattern.CASE_INSENSITIVE);

  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {

      field = field.replace("422623", "42262");

      Matcher match = POUND_DIR_PTN.matcher(field);
      if (match.find()) {
        String dir = ' ' + match.group(1);
        field = stripFieldEnd(field.substring(0,match.start()), dir) + dir + field.substring(match.end());
      }
      super.parse(field, data);
    }
  }

}
