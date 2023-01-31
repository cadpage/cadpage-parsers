package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VAPetersburgParser extends FieldProgramParser {

  public VAPetersburgParser() {
    super("PETERSBURG", "VA",
          "ID UNIT CALL ADDRCITYST! PLACE XST1:X! XST2:X! NOTES:INFO! END");
  }

  @Override
  public String getFilter() {
    return "CAD@petersburg-police.com,cad@petersburg-va.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\| "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\$([A-Z]{1,5}\\d{2}-\\d{6})", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(.*?) *\\bAPT\\b *(.*)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strApt = append(data.strApt, "-", match.group(2));
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
}
