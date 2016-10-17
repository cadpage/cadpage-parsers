package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Talladega County AL
 */
public class ALTalladegaCountyParser extends FieldProgramParser {
  public ALTalladegaCountyParser() {
    super(CITY_LIST, "TALLADEGA COUNTY", "AL",
          "ADDR/S1 ID TIME CALL! geo:GPS? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "Talladega_County_9-1-1@TalladegaCo911.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Talladega_County_9-1-1:");
    return parseFields(body.split(";"), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " X PLACE";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("\\d{4}\\-\\d{6}", true);
    if (name.equals("TIME")) return new TimeField("(?:\\d{2}\\:){2}\\d{2}", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "1 ");
      super.parse(field.replace("@", "&").replace("//", "&"), data);
      if (data.strCity.equals("CHILDERBURG")) data.strCity = "CHILDERSBURG";
    }
  }
  
  private static final String[] CITY_LIST = {
    "ALPINE",
    "BON AIR",
    "CHILDERBURG",     // Misspelled
    "CHILDERSBURG",
    "EASTABOGA",
    "GANTTS QUARRY",
    "LINCOLN",
    "MIGNON",
    "MUNFORD",
    "OAK GROVE",
    "OXFORD",
    "PROVIDENCE",
    "SYCAMORE",
    "SYLACAUGA",
    "TALLADEGA",
    "TALLADEGA SPRINGS",
    "VINCENT",
    "WALDO",
  };
}
