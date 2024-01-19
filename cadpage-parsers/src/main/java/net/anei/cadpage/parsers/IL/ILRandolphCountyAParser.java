package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILRandolphCountyAParser extends FieldProgramParser {

  public ILRandolphCountyAParser() {
    this("RANDOLPH COUNTY", "IL");
  }

  public ILRandolphCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/1 CALL ID PLACE ADDR1 ADDR2 ADDR3 APT CITY ST ZIP NAME PHONE INFO! INFO/N+ " +
          "| CALL_ID ADDRCITY! Caller:NAME Callback_#:PHONE INFO/N+ )");
  }

  @Override
  public String getAliasCode() {
    return "ILRandolphCounty";
  }

  @Override
  public String getFilter() {
    return "lawman@randolphco.org,lawmancm@idsapplications.com,avafire@applecityfd.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // They can use any one of three field delimiters
    String flds[] = body.split(";",-1);

    String[] tFlds = body.split(",", -1);
    if (tFlds.length > flds.length) flds = tFlds;

    tFlds = body.split("~", -1);
    if (tFlds.length > flds.length) flds = tFlds;

    if (flds.length >= 13) {
      setSelectValue("1");
    } else {
      flds = body.split("\n", -1);
      setSelectValue("2");
    }


    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    if (name.equals("ADDR1")) return new MyAddressField(" ");
    if (name.equals("ADDR2")) return new MyAddressField("");
    if (name.equals("ADDR3")) return new MyAddressField(" ", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ST")) return new StateField("[A-Z]{2}|", true);
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("PHONE")) return new PhoneField("[-0-9]*");
    if (name.equals("INFO")) return new MyInfoField();

    if (name.equals("CALL_ID")) return new MyCallIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    private String connect;
    private boolean complete;

    public MyAddressField(String connect) {
      this(connect, false);
    }

    public MyAddressField(String connect, boolean complete) {
      this.connect = connect;
      this.complete = complete;
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, connect, field);
      if (complete) {
        String addr = data.strAddress;
        data.strAddress = "";
        parseAddress(addr, data);
      }
    }
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains(" ")) {
        data.strPlace = append(data.strPlace, " - ", field);
      } else {
        data.strApt = append(data.strApt, "-", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (!ZIP_PTN.matcher(field).matches()) abort();
      if (data.strCity.length() == 0) data.strCity = field;
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("LAT: +[-+]?\\d*\\.\\d+ LON: +[-+]?\\d*\\.\\d+\\b.*");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("Class: *[^ ]*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher match = INFO_GPS_PTN.matcher(line);
        if (match.matches()) {
          setGPSLoc(line, data);
          continue;
        }

        if (INFO_JUNK_PTN.matcher(line).matches()) continue;

        super.parse(line, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }

  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*) (\\d{4}-\\d{6})");
  private class MyCallIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CALL ID";
    }
  }

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*?) +\\d{5}");
  private static final Pattern ADDR_STATE_PTN = Pattern.compile("([A-Z]{2})");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = stripZipCode(p.getLast(','));
      if (ADDR_STATE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = stripZipCode(p.getLast(','));
      }
      data.strCity = city;
      String addr = p.get();
      if (addr.length() == 0) abort();

      String[] parts = addr.split(",");
      int addrNdx;
      switch (parts.length){
      case 1:
        addrNdx = 0;
        break;

      case 2:
        addrNdx = ((checkAddress(parts[0]) > checkAddress(parts[1])) ? 0 : 1);
        break;

      case 3:
        addrNdx = 1;
        break;

      default:
        abort();
        return; // keep compiler happy.
      }

      for (int jj = 0; jj<parts.length; jj++) {
        String part = parts[jj].trim();
        if (jj == addrNdx) {
          parseAddress(part, data);
        } else {
          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY ST";
    }
  }

  private final String stripZipCode(String field) {
    Matcher match = ADDR_ZIP_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    return field;
  }
}
