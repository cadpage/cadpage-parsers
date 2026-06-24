package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ARSebastianCountyBParser extends DispatchH05Parser {

  public ARSebastianCountyBParser() {
    super("SEBASTIAN COUNTY", "AR",
          "SRC? CALL! Address:ADDRCITY! X Date/Time:DATETIME! Narrative:EMPTY! INFO_BLK+ Times:EMPTY! TIMES+? ID! NAME PHONE END");
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "donotreply@sebastiancountyar.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
