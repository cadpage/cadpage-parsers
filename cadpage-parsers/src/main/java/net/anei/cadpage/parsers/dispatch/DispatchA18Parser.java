package net.anei.cadpage.parsers.dispatch;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Supports:
TXCrowley (Tarrant County)
TXHutchins (Dallas County)
TXKeller (Tarrant County)
TXLufkin (Angelina County)
TXGainesville (Cooke County)
TXRoanoke (Denton County)
TXSeabrook (Harris County)
TXSeguin (Guadalupe COunty)
TXDallasCounty

 */

public class DispatchA18Parser extends FieldProgramParser {

  public DispatchA18Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "CALL ADDR X BOX! EMPTY+? ( DASHES EMPTY+? DATETIME SRC SRC | ) INFO+? ID_UNIT ID_UNIT+? END");
  }

  private static final Pattern FIX_BROKEN_INFO_PTN = Pattern.compile("([A-Za-z]+:.*[^\\]])\n(.*\\[\\d\\d:\\d\\d:\\d\\d\\])");


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() > 0) {
      if (!body.startsWith(subject)) body = subject + '\n' + body;
    }

    int pt = body.indexOf("\nDisclaimer");
    if (pt >= 0) {
      body = body.substring(0,pt).trim();
    }
    body = FIX_BROKEN_INFO_PTN.matcher(body).replaceAll("$1$2");
    return parseFields(body.split("\n"), 4, data);
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    int pt = sAddress.indexOf(", Box");
    if (pt >= 0) sAddress = sAddress.substring(0,pt).trim();
    return sAddress;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("DASHES")) return new SkipField("-{5,}", true);
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d.\\d\\d.\\d{4} \\d\\d:\\d\\d:\\d\\d) :.*", true);
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID_UNIT")) return new MyIdUnitField();
    return super.getField(name);
  }

  private static final Pattern PLACE_PTN = Pattern.compile("-(.*[^ ])- +(.*)");
  private static final Pattern REMOVE_DASH_PTN = Pattern.compile("\\b(IH?|ST|RT|HWY)-\\d+");
  private static final Pattern DELIM_PTN = Pattern.compile("[-,]");
  private static final Pattern BOX_PTN = Pattern.compile("^BOX *(\\d+)", Pattern.CASE_INSENSITIVE);
  private static final Pattern DIR_PTN = Pattern.compile("([NSEW]|NORTH|SOUTH|EAST|WEST)\\b *", Pattern.CASE_INSENSITIVE);
  private static final Pattern BOUND_PTN = Pattern.compile("([NSEW]B)S?\\b *", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("^(?:APT|LOT|#) *([-A-Z0-9]+)\\b *", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT2_PTN = Pattern.compile("^([A-Z]?\\d{1,5}[A-Z]?)\\b *", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT3_PTN = Pattern.compile("[A-Z](?:&[A-Z])?", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("-- ")) {
        field = field.substring(3).trim();
      } else if (field.startsWith("-")) {
        Matcher match = PLACE_PTN.matcher(field);
        if (!match.matches()) abort();
        data.strPlace = match.group(1);
        field = match.group(2);
        int pt = data.strPlace.lastIndexOf(" - ");
        if (pt >= 0 && field.toUpperCase().startsWith(data.strPlace.substring(pt+3).trim().toUpperCase())) {
          data.strPlace = data.strPlace.substring(0,pt).trim();
        }
      } else abort();

      // Split up the address field
      field = field.replace('@', '&');
      Matcher match = REMOVE_DASH_PTN.matcher(field);
      if (match.find()) {
        StringBuffer sb = new StringBuffer();
        do {
          match.appendReplacement(sb, match.group().replace('-', ' '));
        } while (match.find());
        match.appendTail(sb);
        field = sb.toString();
      }
      String[] parts = DELIM_PTN.split(field);

      // And try to extract a city from the last fragment
      for (int pt = parts.length-1; pt>=0; pt--) {
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, parts[pt].trim(), data);
        if (data.strCity.length() > 0) {
          String left = getStart();
          if (data.strCity.length() > 0 && left.toUpperCase().endsWith(" IN")) {
            left = left.substring(0,left.length()-3).trim();
          }
          parts[pt] = left;
          break;
        }
      }

      // Now cycle through the remaining partial feild lists
      boolean first = true;
      for (String part : parts) {
        part = part.trim();
        if (part.length() == 0) continue;

        // First segment contains an address and nothing else
        if (first) {
          first = false;
          parseAddress(part, data);
        }

        else {

          match = BOX_PTN.matcher(part);
          if (match.find()) {
            data.strAddress = data.strAddress + ", Box " + match.group(1);
            part = part.substring(match.end()).trim();
          }

          match = DIR_PTN.matcher(part);
          if (match.lookingAt()) {
            data.strAddress = append(data.strAddress, " ", match.group(1));
            part = part.substring(match.end());
          }

          match = BOUND_PTN.matcher(part);
          if (match.lookingAt()) {
            String bnd = match.group(1);
            if (!data.strAddress.toUpperCase().endsWith(bnd.toUpperCase())) {
              data.strAddress = append(data.strAddress, " ", bnd);
            }
            part = part.substring(match.end());
          }

          match = APT_PTN.matcher(part);
          if (match.find()) {
            data.strApt = append(data.strApt, "-", match.group(1));
            part = part.substring(match.end()).trim();
          }

          match = APT2_PTN.matcher(part);
          if (match.find()) {
            data.strApt = append(data.strApt, "-", match.group(1));
            part = part.substring(match.end()).trim();
          }

          if (isValidAddress(part)) {
            if (Character.isDigit(data.strAddress.charAt(0))) {
              data.strCross = append(data.strCross, "/", part);
            } else {
              data.strAddress = append(data.strAddress, " & ", part);
            }
            continue;
          }

          if (APT2_PTN.matcher(part).matches()) {
            data.strApt = append(data.strApt, "-", part);
            continue;
          }
          match = APT3_PTN.matcher(part);
          if (match.matches()) {
            data.strApt = append(data.strApt, "-", part);
            continue;
          }

          data.strPlace = append(data.strPlace, " - ", part);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      if (field.equals("0/0")) field = "";
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldEnd(field, "/NULL");
      field = stripFieldEnd(field, "/0");
      data.strCross = append(data.strCross, "/", field);
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Fire Box =")) abort();
      field = field.substring(10).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile("[A-Za-z]+: *(.*?) *\\[(\\d\\d:\\d\\d:\\d\\d)\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        if (data.strTime.length() == 0) data.strTime = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO TIME";
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("VERIFY")) return;
      data.strSource = append(data.strSource, " ", field.replace(' ', '_'));
    }
  }

  private static final Pattern ID_UNIT_PTN = Pattern.compile("(\\d\\d[A-Z0-9]{2}\\d{6})=([-A-Za-z0-9,]+)");
  private class MyIdUnitField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCallId = append(data.strCallId, "/", match.group(1));
      data.strUnit = append(data.strUnit, ",", match.group(2));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID UNIT";
    }

  }
}