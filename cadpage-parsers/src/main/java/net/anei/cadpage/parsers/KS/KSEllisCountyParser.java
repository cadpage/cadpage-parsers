package net.anei.cadpage.parsers.KS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


public class KSEllisCountyParser extends DispatchH05Parser {

  public KSEllisCountyParser() {
    super("ELLIS COUNTY", "KS",
        "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITYAPT! ID:ID! DATE:DATETIME! INFO:EMPTY! INFO_BLK+ TIMES:EMPTY! TIMES+");
  }

  @Override
  public String getFilter() {
    return "alert@ellisco.net,tnw.ecems@gmail.com,alenser@haysusa.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern US_183_ALT_PTN = Pattern.compile("\\b183 +ALT(?: +HWY)?\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = US_183_ALT_PTN.matcher(addr).replaceAll("183 BYPASS");
    return addr;
  }
}
