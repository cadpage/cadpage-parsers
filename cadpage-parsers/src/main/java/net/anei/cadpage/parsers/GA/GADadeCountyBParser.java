package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class GADadeCountyBParser extends DispatchH05Parser {

  public  GADadeCountyBParser() {
    super("DADE COUNTY", "GA",
          "( SELECT/1 Date:DATETIME! ( Fire_Call:CALL_TYPE! EMS_Call:CALL_TYPE | Call_Type:CALL_TYPE! ) Addr:ADDRCITY/S6! Common_Name:PLACE! City:CITY Cross_St:X! Nature_of_Call:CALL/SDS? Unit:UNIT Primary_Incident:ID ( Info:INFO! | Narrative:INFO! ) INFO/N+ " +
          "| DATETIME ID2 ADDRCITY2/S6 FIRE_CALL_TYPE EMS_CALL_TYPE! UNIT TABLE! TIMES+? TABLE< INFO_BLK+ )", "table");
  }

  @Override
  public String getFilter() {
    return "E911@dadega.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.equals("!")) {
      setSelectValue("1");
      body = body.replace(" Common Name:", "\nCommon Name:");
      return parseFields(body.split("\n"), data);
    } else {
      setSelectValue("2");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ID2")) return new IdField("Incident # *(.*)", true);
    if (name.equals("ADDRCITY2")) return new AddressCityField("Location +(.*)", true);
    if (name.equals("CALL_TYPE")) return new MyCallTypeField(null);
    if (name.equals("FIRE_CALL_TYPE")) return new MyCallTypeField("Fire Call Type");
    if (name.equals("EMS_CALL_TYPE")) return new MyCallTypeField("EMS Call Type");
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("TABLE")) return new SkipField("<table>");
    return super.getField(name);
  }

  private class MyCallTypeField extends CallField {
    private String label;

    public MyCallTypeField(String label) {
      this.label = label;
    }

    @Override
    public void parse(String field, Data data) {
      if (label != null) {
        if (!field.startsWith(label)) abort();
        field = field.substring(label.length()).trim();
      }
      if (!field.equals(data.strCall)) data.strCall = append(data.strCall, "/", field);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
}
