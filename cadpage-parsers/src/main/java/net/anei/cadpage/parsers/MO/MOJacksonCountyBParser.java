package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOJacksonCountyBParser extends FieldProgramParser {

  public MOJacksonCountyBParser() {
    super("JACKSON COUNTY", "MO",
          "( SELECT/RR ID ID/C ADDR CALLCODE TIMES END " +
          "| ID3 ADDR CITY CALL CH MAP! Assigned:UNIT UNIT/C+ Apt/Lot:APT! Location:PLACE END " +
          "| ADDR! ( CITY ZIP | ) CALLCODE! CH! CASE! ID2/C? ( Assigned:UNIT! | UNIT! ) UNIT/C+ )");
  }

  @Override
  public String getFilter() {
    return "totalaccess@mobilfonetotalaccess.com,KCFD.Notify@kcmo.org";
  }

  private static Pattern BODY_DATETIME = Pattern.compile("(.*?)(\\d{2}/\\d{2}/\\d{2})? (\\d{2}:\\d{2}:\\d{2})"); //no space between BODY and DATE
  private static final Pattern RR_DELIM = Pattern.compile(" {3,}");
  private static final Pattern DELIM = Pattern.compile(" *, *| +/// *| *(?=Apt/Lot:|Location:)");
  public boolean parseMsg(String body, Data data) {

    //parse trailing DATE? TIME
    Matcher mat = BODY_DATETIME.matcher(body);
    if (mat.matches()) {
      body = mat.group(1);
      data.strDate = getOptGroup(mat.group(2));
      data.strTime = mat.group(3);
    }

    if (body.contains("First Arrive,")) {
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
      if (!parseFields(RR_DELIM.split(body), data)) return false;
    }

    //try parsing normally
    else {
      setSelectValue("");
      if (!parseFields(DELIM.split(body), data)) return false;
    }

    if (data.strCity.equals("North Kansas Ci")) data.strCity += "ty";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID3")) return new IdField("\\d\\d-\\d{6}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("CALLCODE")) return new MyCallCodeField();
    if (name.equals("CH")) return new ChannelField("(?:(?:[A-Z]+-[A-Z0-9]+|OPS\\d+|DISP)\\b ?)*", true); //only two examples, possibly numeric chars before the "-"?
    if (name.equals("CASE")) return new IdField("Case#:? *((?:[A-Z]+-)?(?:\\d{2}-)?\\d+)", true);
    if (name.equals("ID2")) return new IdField("Response/PCR #(\\d+)|", true);
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?) {3,}(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCity = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyZipField extends ZipField {
    @Override
    public boolean checkParse(String field, Data data) {

      if (!ZIP_PTN.matcher(field).matches()) return false;
      data.strCity = convertCodes(field, CITY_ZIP_TABLE);
      return true;
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {

      // Zip field was a decision field and will be parsed before the city
      // Generally the city field overrides the zip field, but if the new value
      // ends in "COUNTY" and a zip code has been entered, retain the zip code city value
      if (field.isEmpty()) return;
      if (!data.strCity.isEmpty() && field.toUpperCase().endsWith(" COUNTY")) return;
      super.parse(field, data);
    }
  }

  //CALL( / CODE)?
  private static Pattern CALL_CODE = Pattern.compile("(.*?)(?:(?:-| / )(.*))?");
  public class MyCallCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
        Matcher mat = CALL_CODE.matcher(field);
        if (mat.matches()) {
          data.strCall = mat.group(1).trim();
          data.strCode = getOptGroup(mat.group(2));
        } else data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private static final Pattern TIME_BRK_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d)(?=[A-Z])");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = TIME_BRK_PTN.matcher(field).replaceAll("\n");
      field = field.replace(',', ' ');
      data.strSupp = field;
    }
  }

  private static final Properties CITY_ZIP_TABLE = buildCodeTable(new String[] {
      "64012", "Belton",
      "64028", "Farley",
      "64030", "Kansas City",
      "64052", "Independence",
      "64053", "Independence",
      "64055", "Independence",
      "64064", "Lees Summit",
      "64068", "Liberty",
      "64079", "Platte City",
      "64081", "Lees Summit",
      "64092", "Waldron",
      "64101", "Kansas City",
      "64102", "Kansas City",
      "64105", "Kansas City",
      "64106", "Kansas City",
      "64108", "Kansas City",
      "64109", "Kansas City",
      "64110", "Kansas City",
      "64111", "Kansas City",
      "64112", "Kansas City",
      "64113", "Kansas City",
      "64114", "Kansas City",
      "64116", "Kansas City",
      "64117", "Kansas City",
      "64118", "Kansas City",
      "64119", "Kansas City",
      "64120", "Kansas City",
      "64121", "Kansas City",
      "64123", "Kansas City",
      "64124", "Kansas City",
      "64125", "Kansas City",
      "64126", "Kansas City",
      "64127", "Kansas City",
      "64128", "Kansas City",
      "64129", "Kansas City",
      "64130", "Grandview",
      "64131", "Kansas City",
      "64132", "Kansas City",
      "64133", "Kansas City",
      "64134", "Kansas City",
      "64136", "Kansas City",
      "64137", "Kansas City",
      "64138", "Kansas City",
      "64139", "Kansas City",
      "64141", "Kansas City",
      "64145", "Kansas City",
      "64146", "Kansas City",
      "64147", "Kansas City",
      "64148", "Kansas City",
      "64149", "Kansas City",
      "64150", "Riverside",
      "64151", "Kansas City",
      "64152", "Kansas City",
      "64153", "Kansas City",
      "64154", "Kansas City",
      "64155", "Kansas City",
      "64156", "Kansas City",
      "64157", "Kansas City",
      "64158", "Kansas City",
      "64161", "Kansas City",
      "64162", "Kansas City",
      "64163", "Kansas City",
      "64164", "Kansas City",
      "64170", "Kansas City",
      "64171", "Kansas City",
      "64179", "Kansas City",
      "64180", "Kansas City",
      "64183", "Kansas City",
      "64184", "Kansas City",
      "64185", "Kansas City",
      "64187", "Kansas City",
      "64188", "Kansas City",
      "64190", "Kansas City",
      "64191", "Kansas City",
      "64193", "Kansas City",
      "64194", "Kansas City",
      "64195", "Kansas City",
      "64196", "Kansas City",
      "64197", "Kansas City",
      "64198", "Kansas City",
      "64199", "Kansas City",
      "64944", "Kansas City",
      "64999", "Kansas City"
  });
}