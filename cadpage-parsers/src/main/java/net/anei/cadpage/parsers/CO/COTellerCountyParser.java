package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class COTellerCountyParser extends FieldProgramParser {

  public COTellerCountyParser() {
    super("TELLER COUNTY", "CO",
          "( REF:CALL! INC#:ID! Add:ADDR! COMMENT_INFO:INFO! INFO/S+ " +
          "| Add:ADDR! ( Prob:CALL! Loc:PLACE! Incident_#:ID! RP:NAME_PHONE! Comment:INFO " +
                      "| Units:UNIT? Problem:CALL! Apt:APT! Loc:PLACE! Code:CODE! ( RP_Ph:PHONE! | RP_Phone:PHONE! ) GPS:GPS/d? Caution/Access_Info:ACC_INFO/S+ INC#:ID Lat/Long:GPS/d " +
                      ") " +
          ") END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "alerts@eptpaging.info";
  }

  private static final Pattern LOC_CHANGE_PTN = Pattern.compile("REF:(.*) THE LOC HAS CHANGED TO:(.*)#(.*)");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=(?:Problem|Apt|Loc|Code|RP|RP Ph|RP Phone|GPS|Caution/Access Info|Lat/Long):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = LOC_CHANGE_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR APT PLACE");
      data.strCall = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strPlace = match.group(3).trim();
      return true;
    } else {
      body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PHONE")) return new PhoneField("(?:\\d+\\) *)(.*)");
    if (name.equals("ACC_INFO")) return new MyAccessInfoField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern ALI_INFO_PTN = Pattern.compile(" *911ALI Info[': ]*");
  private static final Pattern START_INFO_JUNK_PTN = Pattern.compile("^[-&. A-Za-z0-9]+, Wireless, [-0-9]+[, ]*");
  private static final Pattern END_INFO_JUNK_PTN = Pattern.compile("[, ]*(?:\\[[^\\]]*|Multi-[^,]*|Automatic Case[^,]*)$");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *(?:Multi-(?:Agency|Jurisdiction) [ A-Za-z]+? Incident #: [A-Z]*[-0-9]*|A cellular re-bid has occurred, check the ANI/ALI Viewer for details|Automatic Case Number\\(s\\)[^,]+?,|\\[Shared\\]|\\[ProQA: Case Entry Complete\\]|\\[ProQA Session Aborted\\]),? *");

  private class MyAccessInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "False");
      field = stripFieldStart(field, "True");

      for (String part : ALI_INFO_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", cleanInfo(part));
      }
    }

    private String cleanInfo(String field) {
      field = START_INFO_JUNK_PTN.matcher(field).replaceFirst("");
      field = END_INFO_JUNK_PTN.matcher(field).replaceFirst("");
      StringBuilder sb = new StringBuilder();
      for (String part : INFO_JUNK_PTN.split(field)) {
        if (!part.isEmpty() && !part.equals("q")) {
          if (sb.length() > 0) sb.append(' ');
          sb.append(part);
        }
      }
      return stripFieldEnd(sb.toString(), ",");
    }
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) *\\b(\\d{3}[- ]?\\d{3}[- ]?\\d{4})");
  private class MyNamePhoneField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field =  match.group(1);
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }


  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1364 CR 75",                           "+39.045712,-105.094569",
      "27668 HWY 67",                         "+39.029401,-105.070055",
      "28541 N HWY 67",                       "+39.052499,-105.094141",
      "16006 W HWY 24",                       "+38.974109,-105.075742",
      "16420 W HWY 24",                       "+38.975533,-105.074088"

 });
}
