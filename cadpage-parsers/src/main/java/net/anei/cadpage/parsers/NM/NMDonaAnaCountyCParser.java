package net.anei.cadpage.parsers.NM;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class NMDonaAnaCountyCParser extends DispatchH05Parser {

  public NMDonaAnaCountyCParser() {
    super("DONA ANA COUNTY", "NM",
          "( DATETIME ID ADDRCITY X UNIT INFO_BLK+ " +
          "| CALL ADDRCITY SRC DATETIME UNIT ID EMPTY! INFO_BLK+? TIMES+ )");
  }

  @Override
  public String getFilter() {
    return "@mvrda.org";
  }

  @Override
  protected boolean parseFields(String[] flds, Data data) {

    // First field is one conglomeration of multiple fields separated by semicolons
    if (flds.length == 0) return false;
    String[] flds1 = flds[0].split(";", -1);
    if (flds1.length > 1) {
      String[] flds2 = new String[flds1.length+flds.length-1];
      System.arraycopy(flds1, 0, flds2, 0, flds1.length);
      System.arraycopy(flds, 1, flds2, flds1.length, flds.length-1);
      flds = flds2;
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
