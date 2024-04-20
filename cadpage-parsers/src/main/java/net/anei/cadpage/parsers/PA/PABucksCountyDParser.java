package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PABucksCountyDParser extends FieldProgramParser {

  public PABucksCountyDParser() {
    super("BUCKS COUNTY", "PA",
          "CALL! Inc#:ID! ( Box:BOX! Alrm_Lvl:PRI! ( Place_Name:PLACE! Addr:ADDR! | Addr:ADDR! Place_Name:PLACE! ) btwn:X! Pri_Remarks:INFO ( Call_Text:INFO/N! | Text:INFO/N! ) Channel:CH! ( Units:UNIT! | Run:UNIT! ) GPS! TIME! Caller:NAME Phone:PHONE Alt:PHONE/L " +
                         "| Units:UNIT! Place_Name:PLACE Addr:ADDR! btwn:X! GPS! ( Box:BOX! | FIRE_Box:BOX! EMS_Box:BOX/L! ) ( Pri_Remarks:PRI! | PRI_Remarks:PRI! ) Call_Text:INFO! Caller:NAME! Phone:PHONE! Alt_Ph:PHONE/L! Alrm_Lvl:PRI! Channel:CH! ( Time_Out:TIME! | TIME! ) G:SKIP D:SKIP " +
                         "| Sector:MAP! Place_Name:PLACE! Addr:ADDR! btwn:X! Pri_Remarks:PRI! Call_Text:INFO! GPS! TIME! Units:UNIT! Caller:NAME Phone:PHONE Alt:PHONE/L " +
                         ") END ");
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

    int pt = body.indexOf("\n******");
    if (pt >= 0) body = body.substring(0,pt).trim();

    if (subject.equals("CAD PAGE")) {
      setFieldList("CALL INFO");
      if (body.startsWith("COVER NOTIFICATION:,")) {
        data.msgType = MsgType.GEN_ALERT;
        data.strCall = "COVER NOTIFICATION";
        for (String line : body.substring(20).split(",")) {
          data.strSupp = append(data.strSupp, "\n", line.trim());
        }
        return true;
      }
      else {
        String[] flds = body.split("\n");
        if (flds.length >= 10) {
          return parseFields(flds, data);
        } else {
          setFieldList("INFO");
          data.msgType = MsgType.GEN_ALERT;
          data.strSupp = body;
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("-")) return;
      super.parse(field, data);
    }
  }
}
