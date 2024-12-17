package net.anei.cadpage.parsers.MO;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOMillerCountyParser extends FieldProgramParser {

  public MOMillerCountyParser() {
    super("MILLER COUNTY", "MO",
          "PLACE NAME:NAME! PHONE:PHONE! ADDRESS:EMPTY! ADDR! Intersection:X! CALL! CALL/SDS+ NOTES:EMPTY! INFO/N+");
  }

  private String subject;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = subject.indexOf('(');
    if (pt >= 0) subject = subject.substring(0, pt).trim();
    this.subject = subject;
    pt = body.indexOf("\f");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern APT_ADDR_PTN = Pattern.compile("(\\d+) (\\S*\\d\\S*)(?<!ST|ND|RD|TH) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*) (\\S*\\d\\S*)\\b *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");

      // The original subject contains the unit info, then the address, and finally a city.
      // Usually we can find the address field in the subject and use that to split
      // everything out.
      String units;

      do {
        int pt = subject.indexOf(field);
        if (pt >= 0) {
          units = subject.substring(0, pt).trim();
          data.strCity = subject.substring(pt+field.length()).trim();
          break;
        }

        // If that does not work, there is probably an apartment field.  The apartment
        // is found in both the address field and the subject, but in a different location.  In
        // in the address field, it is always the second token following the house number
        Matcher match = APT_ADDR_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1) + ' ' + match.group(3);
          data.strApt = match.group(2);

          // Usually the apartment follows the address in the subject field, but occasionally
          // it is not there at all.
          match = ADDR_APT_PTN.matcher(subject);
          if (match.matches() && data.strApt.equals(match.group(2))) {
            String addr = match.group(1);
            data.strCity = match.group(3);
            units = stripFieldEnd(addr, field);
            if (units.length() == addr.length()) units = "";
            break;
          } else {
            pt = subject.indexOf(field);
            if (pt >= 0) {
              units = subject.substring(0, pt).trim();
              data.strCity = subject.substring(pt+field.length()).trim();
              break;
            }
          }
        }

        // No go making sense out of this :(
        units = "";
      } while (false);

      // Parse the identified address field
      super.parse(field, data);

      // If we identified a unit field, clean it up
      if (!units.isEmpty()) {
        units = units.replace("Miller County S.O.", "MCSO")
                     .replace("Lake Ozark Police", "LOPD")
                     .replace("Camden Co FD", "CCFD")
                     .replace("Camden Co PD", "CCPD")
                     .replace("Eldon FD", "EFD")
                     .replace("Eldon PD", "EPD")
                     .replace("Osage Beach FD", "OBFD")
                     .replace("Osage Beach PD", "OBPD")
                     .replace("Pulaski Co Sheriff Office", "PCSO");
          StringBuilder unitList = new StringBuilder();
          Set<String> unitSet = new HashSet<>();
          for (String unit : units.split(" +")) {
            if (unitSet.add(unit)) {
              if (unitList.length() > 0) unitList.append(',');
              unitList.append(unit);
            }
          }
          data.strUnit = unitList.toString();
        }
      }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
}
