package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PABucksCountyDParser extends FieldProgramParser {

  public PABucksCountyDParser() {
    super("BUCKS COUNTY", "PA",
          "CALL! ( Inc#:ID! ( Box:BOX! Alrm_Lvl:PRI! ( Place_Name:PLACE! Addr:ADDR! | Addr:ADDR! Place_Name:PLACE! ) btwn:X! Pri_Remarks:INFO ( Call_Text:INFO/N! | Text:INFO/N! ) Channel:CH! ( Units:UNIT! | Run:UNIT! ) GPS! TIME! Caller:NAME Phone:PHONE Alt:PHONE/L " +
                           "| Units:UNIT! Place_Name:PLACE Addr:ADDR! btwn:X! GPS! ( Box:BOX! | FIRE_Box:BOX! EMS_Box:BOX/L! | Fire_Box#:BOX! EMS_Box#:BOX/L! | Fire_Box:BOX! EMS_Box:BOX/L! | Sector:MAP! ) ( Pri_Remarks:PRI! | PRI_Remarks:PRI! ) Call_Text:INFO! ( Caller:NAME! | Caller_Name:NAME! Caller_Adr:SKIP! Caller_Apt#:SKIP! ) Phone:PHONE! Alt_Ph:PHONE/L! Alrm_Lvl:PRI! Channel:CH! ( Time_Out:TIME! | TIME! ) G:SKIP D:SKIP " +
                           "| Sector:MAP! Place_Name:PLACE! Addr:ADDR! btwn:X! Pri_Remarks:PRI! Call_Text:INFO! GPS! TIME! Units:UNIT! Caller:NAME Phone:PHONE Alt:PHONE/L " +
                           ") END " +
                "| Box:BOX! ADDR! btwn:X! GPS! Text:INFO! TIME_ID! Run:UNIT! " +
                ")");
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
    if (name.equals("TIME_ID"))  return new MyTimeIdField();
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

  private static Pattern TIME_ID_PTN = Pattern.compile("(\\d\\d?:\\d\\d:\\d\\d) +(.*)");
  private class MyTimeIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "TIME ID";
    }
  }
}
