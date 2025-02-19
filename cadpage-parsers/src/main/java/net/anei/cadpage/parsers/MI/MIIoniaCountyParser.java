package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIIoniaCountyParser extends FieldProgramParser {

  public MIIoniaCountyParser() {
    super("IONIA COUNTY", "MI",
          "Location:ADDRCITYST! ESN:SKIP! Cross_Streets:X! Call_Details:INFO! Use_Caution:ALERT! ProQA:INFO/N! All_ProQA:INFO2! Latitude:GPS1! Longitude:GPS2! CFS#:ID! END");
  }

  @Override
  public String getFilter() {
    return "noreplyzuercher@ioniacounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Code:")) return false;
    data.strCall = subject.substring(5).trim();
    return super.parseMsg(body,  data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ALERT")) return new MyAlertField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PLACE_PTN1 = Pattern.compile("(.*) (?:None|(?i:apt|in|lot|rm|room) (\\S+))\\b *(.*)");
  private static final Pattern ADDR_APT_PLACE_PTN2 = Pattern.compile("(.*) (\\d{1,3}|dining)\\b *(.*)");
  private static final Pattern ADDR_APT_PLACE_PTN3 = Pattern.compile("(.* [A-Z]{2} \\d{5}) +()(.*)");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_APT_PLACE_PTN1.matcher(field);
      if (!match.matches()) {
        match = ADDR_APT_PLACE_PTN2.matcher(field);
        if (!match.matches()) match = ADDR_APT_PLACE_PTN3.matcher(field);
        if (!match.matches()) {
          if (field.contains(",")) {
            super.parse(field, data);
          } else {
            MIIoniaCountyParser.this.parseAddress(StartType.START_ADDR, 0, field, data);
            data.strPlace = getLeft();
          }
          return;
        }
      }
      super.parse(match.group(1).trim(), data);
      data.strApt = append(data.strApt, "-", getOptGroup(match.group(2)));
      data.strPlace =  match.group(3);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Yes")) data.strAlert = "Use Caution!!";
    }
  }

  private static final Pattern INFO2_BRK_PTN = Pattern.compile(" +(?=\\d{1,2}\\.)");

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO2_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
