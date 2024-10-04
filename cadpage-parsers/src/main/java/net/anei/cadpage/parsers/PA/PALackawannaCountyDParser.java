package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALackawannaCountyDParser extends FieldProgramParser {

  public PALackawannaCountyDParser() {
    super(CITY_CODES, "LACKAWANNA COUNTY", "PA",
          "Call_Type:CALL! Location:ADDRCITY! Units:UNIT! Priority:PRI! CAD:ID!  Radio_Channel:CH! Area:MAP! Time:TIME! END");
  }

  @Override
  public String getFilter() {
    return "lackawannafirepager@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Luzerne Fire Automated Alert")) return false;
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("#(F\\d+)", true);
    return super.getField(name);
  }

  private static final Pattern BAD_CITY_PTN = Pattern.compile("\\d+");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);

      // City code table is very sparse, so if we find an unknonw entry, clear it
      if (BAD_CITY_PTN.matcher(data.strCity).matches()) data.strCity = "";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "24", "MOUNTAIN TOP",
      "31", "FAIRMOUNT TWP",
      "33", "SCRANTON",
      "37", "ASHLEY",
      "48", "JENKINS TWP",
      "66", "PITSTON TWP",
      "68", "BLAKELY",
      "69", "SCRANTON",
      "88", "WILKES-BARRE TWP"
  });
}
