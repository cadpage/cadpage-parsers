package net.anei.cadpage.parsers.CT;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Middletown, CT
 */
public class CTMiddletownParser extends FieldProgramParser {

  public CTMiddletownParser() {
    this("MIDDLETOWN","CT");
  }

  CTMiddletownParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "( ID CALL PLACE ADDR APT CITY ZIP X UNIT DATETIME/d! INFO/N+ " +
          "| UNIT CALL PLACE ADDR APT CITY? ZIP? ID? INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "NexgenMessage@middletownctpolice.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\\|"), 3, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern LEAD_ZERO_PTN = Pattern.compile("^0+");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = squeeze(field);
      if (field.equals(squeeze(data.strPlace))) data.strPlace = "";
      super.parse(field, data);
    }

    private String squeeze(String field) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      field = LEAD_ZERO_PTN.matcher(field).replaceFirst("");
      return field;
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:Apt|Unit|Room|Lot)[ #]*(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[] {
      "CROMWELL",
      "DURHAM",
      "EAST HAMPTON",
      "HADDAM",
      "HIGGANUM",
      "MIDDLEFIELD",
      "MIDDLETOWN",
      "PORTLAND"
  };
}
