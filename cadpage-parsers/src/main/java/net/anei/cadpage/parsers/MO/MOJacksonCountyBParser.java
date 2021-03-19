package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOJacksonCountyBParser extends FieldProgramParser {

  public MOJacksonCountyBParser() {
    super("JACKSON COUNTY", "MO",
          "( SELECT/RR ID ID/C ADDR CALLCODE TIMES END " +
          "| ID3 ADDR CITY CALL CH MAP! Assigned:UNIT UNIT/C+ Apt/Lot:APT! Location:PLACE END " +
          "| ADDR! CALLCODE! CH! CASE! ( ID2/C! Assigned:UNIT! | UNIT! ) UNIT/C+ )");
  }

  @Override
  public String getFilter() {
    return "totalaccess@mobilfonetotalaccess.com,KCFD.Notify@kcmo.org";
  }

  private static Pattern BODY_DATETIME = Pattern.compile("(.*?)(\\d{2}/\\d{2}/\\d{2})? (\\d{2}:\\d{2}:\\d{2})"); //no space between BODY and DATE
  private static final Pattern RR_DELIM = Pattern.compile(" {3,}");
  private static final Pattern DELIM = Pattern.compile(" *, *| +/// *| *(?=Apt/Lot:|Location:)");
  public boolean parseMsg(String body, Data data) {

    //parse trailing DATE? TIME
    Matcher mat = BODY_DATETIME.matcher(body);
    if (mat.matches()) {
      body = mat.group(1);
      data.strDate = getOptGroup(mat.group(2));
      data.strTime = mat.group(3);
    }

    if (body.contains("First Arrive,")) {
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
      if (!parseFields(RR_DELIM.split(body), data)) return false;
    }

    //try parsing normally
    else {
      setSelectValue("");
      if (!parseFields(DELIM.split(body), data)) return false;
    }

    if (data.strCity.equals("North Kansas Ci")) data.strCity += "ty";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID3")) return new IdField("\\d\\d-\\d{6}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALLCODE")) return new MyCallCodeField();
    if (name.equals("CH")) return new ChannelField("(?:(?:[A-Z]+-[A-Z0-9]+|OPS\\d+)\\b ?)*", true); //only two examples, possibly numeric chars before the "-"?
    if (name.equals("CASE")) return new IdField("Case# *((?:[A-Z]+-)?(?:\\d{2}-)?\\d+)", true);
    if (name.equals("ID2")) return new IdField("Response/PCR #(\\d+)", true);
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?) {3,}(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCity = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  //CALL( / CODE)?
  private static Pattern CALL_CODE = Pattern.compile("(.*?)(?:(?:-| / )(.*))?");
  public class MyCallCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
        Matcher mat = CALL_CODE.matcher(field);
        if (mat.matches()) {
          data.strCall = mat.group(1).trim();
          data.strCode = getOptGroup(mat.group(2));
        } else data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private static final Pattern TIME_BRK_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d)(?=[A-Z])");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = TIME_BRK_PTN.matcher(field).replaceAll("\n");
      field = field.replace(',', ' ');
      data.strSupp = field;
    }
  }
}