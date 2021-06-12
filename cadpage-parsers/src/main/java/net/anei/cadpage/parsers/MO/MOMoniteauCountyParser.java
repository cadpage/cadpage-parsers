package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOMoniteauCountyParser extends HtmlProgramParser {

  public MOMoniteauCountyParser() {
    this("MONITEAU COUNTY", "MO");
  }

  public MOMoniteauCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/HTML Call:EMPTY! CALL! Sub:EMPTY! CALL/S Place:EMPTY! PLACE Address:EMPTY! ADDR City:EMPTY! CITY Apt:EMPTY! APT Cross_Streets:EMPTY! X Event#:EMPTY! ID! Reporting_Person:EMPTY! NAME Phone#:EMPTY! PHONE Initiated:EMPTY! SKIP! INFO/N+ Units%EMPTY! UNIT/C+?  COPYRIGHT " +
          "| Call:CALL! Sub:CALL/S! Place:PLACE! Address:ADDR! ( City:CITY! Apt:APT! | Apt:APT! City:CITY! State:ST! Zip:ZIP! ) Cross_Streets:X! Event#:ID! Reporting_Person:NAME! Phone#:PHONE! ( Unit:UNIT! Initiated:SKIP! Notes:INFO! INFO/N+ | Initiated:SKIP! INFO/N+ Units%EMPTY! UNIT/C+ ) " +
          "| ADDR! Apt:APT! CITY! Cross_Streets:X! Event#:ID! Reporting_Person:NAME! Phone#:PHONE! INFO/N+ Units%EMPTY! UNIT/C+ )");
  }

  @Override
  public String getAliasCode() {
    return"MOMoniteauCounty";
  }

  @Override
  public String getFilter() {
    return "CAD@MONITEAU911.COM,CADMONITEAU911@OMNIGO.COM,DISPATCH@OMNIGO.COM,noreply@omnigo.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<html>")) {
      setSelectValue("HTML");
      int pt = body.indexOf("\n©");
      if (pt >= 0) body  = body.substring(0,pt).trim();
      return super.parseHtmlMsg(subject, body, data);
    } else {
      setSelectValue("");
      return parseMsg(body, data);
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("COPYRIGHT")) return new SkipField("©.*", true);
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Call Received on ")) return;
      super.parse(field, data);
    }
  }
}
