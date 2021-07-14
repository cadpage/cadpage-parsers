package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALEtowahCountyCParser extends FieldProgramParser {

  public ALEtowahCountyCParser() {
    super("ETOWAH COUNTY", "AL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! INFO:INFO! Description:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "southsidedispatch@southsideal.city,Active911Dispatch@calcoso.org";
  }

  private static final Pattern DELIM = Pattern.compile("\n|/{2,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("SSFD Alert")) return false;
    return parseFields(DELIM.split(body), data);
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strPlace)) data.strPlace = "";
      super.parse(field, data);
    }
  }

}
