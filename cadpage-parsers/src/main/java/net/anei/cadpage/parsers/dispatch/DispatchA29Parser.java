package net.anei.cadpage.parsers.dispatch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class DispatchA29Parser extends FieldProgramParser {

  public DispatchA29Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, null);

    String extra = (getExtraParseAddressFlags() & FLAG_IMPLIED_INTERSECT) != 0 ? "/i" : "";
    setProgram("CALL:CALL! PLACE:PLACE? ADDR:ADDR"+extra+"! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! MAP:MAP UNIT:UNIT INFO:INFO", 0);
  }

  private static final Pattern MARKER1 = Pattern.compile("^DISPATCH[:\n](?:([-_A-Z0-9]+)(?: |\n\n))?(?:([^-]\\S*: *\\S+(?: (?:FD|ALS|BLS))?) )?");
  private static final Pattern MARKER2 = Pattern.compile("(?:(\\d\\d?/\\d\\d?) (\\d\\d?:\\d\\d?) - )?(?:([A-Z]{2,4}:\\d{2}-\\d{6}) )?");
  private static final Pattern CODE_PTN = Pattern.compile("([A-Z0-9]+) +");
  private static final Pattern UNIT_INFO_PTN = Pattern.compile("[ /\n]+((?:\\b[A-Z\\d]+:[-_A-Z\\d]+(?: FD|-\\d| \\d(?=,)|)\\b,?)++)[ /\n]*");
  private static final Pattern NEW_LINE_PTN = Pattern.compile("\n+");
  private static final Pattern HOUSE_NUMBER_PTN = Pattern.compile("[ /]*\\b(?<!(?:\\b(?:RT|US|HWY|APT|RM)|#) ?)([-*0-9]+) *([NSEW]|BLK|BLOCK|), +");
  private static final Pattern MULT_SLASH_PTN = Pattern.compile("//+");
  private static final Pattern DIR_OF_PTN = Pattern.compile("[/ ]+((?:N|S|E|W|NO|SO|EA|WE|NORTH|SOUTH|EAST|WEST) OF)[/ ]+");
  private static final Pattern CALL_ADDR_DELIM = Pattern.compile("/(?! *(?:AMBULANCE|MEDICAL|MISDIAL|RESCUE|SEIZURES))");

  @Override
  public boolean parseMsg(String body, Data data) {

    Matcher match = MARKER1.matcher(body);
    if (!match.find()) return false;
    data.strSource = getOptGroup(match.group(1));
    data.strUnit = getOptGroup(match.group(2)).replace(" ", "");
    body = body.substring(match.end()).trim();
    if (data.strSource.length() == 0 && data.strUnit.length() == 0) return false;

    boolean good = body.startsWith("- ");
    if (good) body = body.substring(2).trim();

    body = body.replace("Apt/Unit", "Apt");

    if (body.startsWith("CALL:")) {
      return super.parseMsg(body, data);
    }

    match = MARKER2.matcher(body);
    if (!match.lookingAt()) return false;
    setFieldList("DATE TIME ID CODE CALL ADDR APT CITY PLACE PHONE INFO");

    data.strDate = getOptGroup(match.group(1));
    data.strTime = getOptGroup(match.group(2));
    data.strCallId = getOptGroup(match.group(3));
    body = body.substring(match.end()).trim();
    if (!good && data.strCallId.length() == 0) return false;

    // Split of call code
    match = CODE_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCode = match.group(1);
      body = body.substring(match.end());
    }

    // Look for unit field.  If found is separates sup info from rest of call
    match = UNIT_INFO_PTN.matcher(body);
    if (match.find()) {
      addUnit(match.group(1), data);
      data.strSupp = body.substring(match.end());
      body = body.substring(0,match.start());
    }

  // Reduce multiple slashes to single slash
  body = MULT_SLASH_PTN.matcher(body).replaceAll("/");

    // Look for a line break separating the call and address
    boolean lock = false;
    StartType st = StartType.START_CALL;
    String[] parts = NEW_LINE_PTN.split(body);
    if (parts.length >= 2) {
      lock = true;
      data.strCall = parts[0].trim();
      body = parts[1].trim();
      st = StartType.START_ADDR;

      if (parts.length >= 3) {
        addPlace(parts[2].trim(), data);
        if (parts.length >= 4) return false;
      }

      // Remove comma following house number
      match = HOUSE_NUMBER_PTN.matcher(body);
      if (match.lookingAt()) {
        body = append(append(match.group(1), " ", match.group(2)), " ", body.substring(match.end()));
      }
    }

    // Otherwise, we have to do this the hard way
    else {

      // Now things get complicated.
      // See if we can find an odd street number convention that marks the end of the
      // call description
      match = HOUSE_NUMBER_PTN.matcher(body);
      if (match.find()) {
        data.strCall = body.substring(0,match.start()).trim();
        body = append(append(match.group(1), " ", match.group(2)), " ", body.substring(match.end()).trim());
        st = StartType.START_ADDR;
      }
    }

    // There is always a comma followed by a (possibly empty) city and optional place name
    // Very occasionally, the comma is part of the place name field, so we also check for
    // a city name following the second comma.
    int pt = body.lastIndexOf(',');
    if (pt < 0) return false;
    String city = body.substring(pt+1).trim();
    city = stripFieldStart(city, "/");
    Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city);
    if (!res.isValid()) {
      int pt2 = body.lastIndexOf(',', pt-1);
      if (pt2 >= 0) {
        city = body.substring(pt2+1);
        Result res2 = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city);
        if (res2.isValid()) {
          pt = pt2;
          res = res2;
        }
      }
    }
    res.getData(data);
    addPlace(stripFieldStart(res.getLeft(),"/"), data);
    body = body.substring(0,pt).trim();

    // OK, see what we can do with the address
    int flags = FLAG_ANCHOR_END;
    if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
    if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
    flags |= getExtraParseAddressFlags();

   // Dispatch doesn't (or didn't) use the normal & or / intersection convention, the
   // use long NORTH OF type constucts.  To get these through the
   // smart address parser, we need to replace them all with &
   // then change them back on the other side.  To keep things
   // simple, we assume no more than one such construct per address
   String connector = null;
   match = DIR_OF_PTN.matcher(body);
   if (match.find()) {
     connector = match.group(1);
     body = body.substring(0,match.start()) + " & " + body.substring(match.end());
   }
   body = body.replace('@', '&');
   parseAddress(st, flags, body, data);

   // Sometimes there is a slash delimiter marking the end of the call description.  It is not
   // always there, and we couldn't check for it earlier lest we confuse an intersection address
   // for the delimiter.  But now that we have done the best we could with the address, see if
   // we can find that delimiter in the call description, and if we can, move everything behind
   // it into the address
   if (data.strAddress.startsWith("&")) {
     data.strAddress = data.strAddress.substring(1).trim();
     if (connector != null) data.strAddress = data.strAddress.replaceFirst("&", connector);
   } else {
     if (connector != null) data.strAddress = data.strAddress.replaceFirst("&", connector);
     if (!lock) {
       CodeSet callList = getCallList();
       if (callList == null || callList.getCode(data.strCall) == null) {
         match = CALL_ADDR_DELIM.matcher(data.strCall);
         if (match.find()) {
           pt = match.start();
           String addr = append(data.strCall.substring(pt+1), " ", data.strAddress);
           data.strCall = data.strCall.substring(0,pt).trim();
           data.strAddress = "";
           parseAddress(addr, data);
         }
       }
     }
   }
   return true;
 }

  @Override
  public String getProgram() {
    return "SRC UNIT " + super.getProgram();
  }

  private void addUnit(String field, Data data) {
    Set<String> unitSet = new HashSet<String>();
    if (data.strUnit.length() > 0) unitSet.addAll(Arrays.asList(data.strUnit.split(",")));
    field = field.replace(" ", "");
    for (String unit : field.split(",")) {
      if (!unitSet.contains(unit)) {
        unitSet.add(unit);
        data.strUnit = append(data.strUnit, ",", unit);
      }
    }
  }

  private void addPlace(String field, Data data) {
    Matcher match = CALLBK_PTN.matcher(field);
    if (match.find()) {
      data.strPhone = match.group(1);
      field = append(field.substring(0,match.start()).trim(), " ", field.substring(match.end()).trim());
    }
    field = stripFieldStart(field, "ALIAS=");
    data.strPlace = append(field, " - ", data.strPlace);
  }
  private static final Pattern CALLBK_PTN = Pattern.compile("\\bCALLBK=([-\\d]*)");

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("(\\d\\d:\\d\\d:\\d\\d)|[0-9:]*()", true);
    if (name.equals("UNIT"))  return new MyUnitField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strCode = match.group(1);
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class  MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      addUnit(field, data);
    }
  }
}
