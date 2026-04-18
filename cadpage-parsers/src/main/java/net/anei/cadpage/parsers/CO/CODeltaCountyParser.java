package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CODeltaCountyParser extends FieldProgramParser {

  public CODeltaCountyParser() {
    super(CITY_CODES, "DELTA COUNTY", "CO",
          "CALL:CALL! ADDR:ADDR! CITY:CITY ID:ID UNIT:UNIT INFO:INFO");
  }

  @Override
  public String getFilter() {
    return "tech@dcadems.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern ADDR_SPLIT_PTN = Pattern.compile("(.*)[;,:](.*)");
  private static final Pattern ADDR_APT_PTN1 = Pattern.compile("(.*)\\b(?:APT|UNIT|RM|ROOOM|LOT) +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT_PTN2 = Pattern.compile("[A-Z]?\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      while (true) {
        Matcher match = ADDR_SPLIT_PTN.matcher(field);
        if (!match.matches()) break;
        field = match.group(1).trim();
        String place = match.group(2).trim();
        match = ADDR_APT_PTN1.matcher(place);
        if (match.matches()) {
          place = match.group(1);
          String tmp = match.group(2);
          if (tmp == null) tmp = match.group(3);
          apt = append(tmp, "-", apt);
        } else if (ADDR_APT_PTN2.matcher(place).matches()) {
          apt = append(place, "-", apt);
          place = "";
        }
        data.strPlace = append(place, " - ", data.strPlace);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AUS", "Austin",
      "MON", "Montrose",
      "SOM", "Somerset"
  });
}
