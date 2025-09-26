package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOGreeneCountyCParser extends HtmlProgramParser {

  public MOGreeneCountyCParser() {
    super("GREENE COUNTY", "MO",
          "Call:CALL/SDS! Address:ADDRCITY! Coordinates:GPS! Address_Comment:COMMENT! Resource:MAP! Response:PRI! Notes:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "sa@logis.dk";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String saveAddress;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    data.strCall = subject;
    saveAddress = "";
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("COMMENT")) return new MyCommentField();
    return super.getField(name);
  }

  private static final Pattern ZIP_CITY_PTN = Pattern.compile("\\d{5} +(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      saveAddress = field;

      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        field = field.substring(0,pt).trim();
      }

      super.parse(field, data);

      Matcher match = ZIP_CITY_PTN.matcher(data.strCity);
      if (match.matches()) data.strCity = match.group(1);
    }
  }

  private class MyCommentField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(saveAddress)) return;
      super.parse(field, data);
    }
  }
}
