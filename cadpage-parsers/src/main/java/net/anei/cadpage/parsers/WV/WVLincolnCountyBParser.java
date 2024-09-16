package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class WVLincolnCountyBParser extends FieldProgramParser {

  public WVLincolnCountyBParser() {
    super("LINCOLN COUNTY", "WV",
          "CALL ADDR_X ( SELECT/1 NAME PHONE ID! | CITY NAME_PHONE EMPTY ID EMPTY UNIT_TIMES:INFO/R! ) INFO/RN+");
  }

  @Override
  public String getFilter() {
    return "lincowv911@e911.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Lincoln 911:")) {
      setSelectValue("2");
      data.msgType = MsgType.RUN_REPORT;
      body = body.substring(12).trim();
    } else {
      setSelectValue("1");
    }
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_X")) return new MyAddressCrossField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    return super.getField(name);
  }

  private static final Pattern ADDR_X_PTN = Pattern.compile("(.*)(?: {2,}| +XS: *)(.*)");

  private class MyAddressCrossField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_X_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCross = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  private class MyNamePhoneField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" (");
      if (pt >= 0) {
        data.strPhone = field.substring(pt+1);
        field = field.substring(0, pt).trim();
      }
      data.strName = cleanWirelessCarrier(field);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }
}
