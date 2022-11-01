package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALBibbCountyParser extends FieldProgramParser {

  public ALBibbCountyParser() {
    super("BIBB COUNTY", "AL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! INFO:INFO/N");
  }

  @Override
  public String getFilter() {
    return "psmith@bibbcosoal.org";
  }

  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("([A-Z0-9]+) Alert");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_SRC_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strPlace)) data.strPlace = "";
      super.parse(field, data);
    }
  }

  private class  MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("//")) {
        line = line.trim();
        line = stripFieldStart(line, "Subject:");
        line = stripFieldStart(line, "Description:");
        super.parse(line, data);
      }
    }
  }
}
