package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PABucksCountyDParser extends FieldProgramParser {

  public PABucksCountyDParser() {
    super("BUCKS COUNTY", "PA",
          "CALL! Inc#:ID! Box:BOX! Alrm_Lvl:PRI! Place_Name:PLACE! Addr:ADDR! btwn:X! Pri_Remarks:INFO! ( Call_Text:INFO/N! | Text:INFO/N! ) Channel:CH! Units:UNIT! GPS! TIME! END");
  }

  @Override
  public String getFilter() {
    return "mss@buckscounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = body.indexOf("\n\n***");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (subject.equals("CAD PAGE")) {
      return parseFields(body.split("\n"), data);
    }

    if (subject.startsWith("CAD Report")) {
      data.msgType = MsgType.RUN_REPORT;
      setFieldList("INFO");
      data.strSupp = body;
      return true;
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
