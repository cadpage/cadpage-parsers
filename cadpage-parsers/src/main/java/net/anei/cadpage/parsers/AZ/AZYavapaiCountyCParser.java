package net.anei.cadpage.parsers.AZ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class AZYavapaiCountyCParser extends FieldProgramParser {

	public AZYavapaiCountyCParser() {
		super(CITY_CODES, "YAVAPAI COUNTY", "AZ",
		      "SRC ( BOX | SRC? ) CALL ADDR UNIT/S! INFO/N+? ( EMPTY ID? | ID ) EMPTY+? X? DATETIME?");
		setupGpsLookupTable(GPS_LOOKUP_TABLE);
	}

	@Override
	public boolean parseMsg(String subject, String body, Data data) {

	  if (!subject.equals("Message from HipLink")) return false;

	  return parseFields(body.split("\n"), data);
	}

	@Override
  public Field getField(String name) {
	  if (name.equals("SRC")) return new MySourceField();
	  if (name.equals("BOX")) return new BoxField("\\d+", true);
	  if (name.equals("ADDR")) return new MyAddressField();
	  if (name.equals("INFO")) return new MyInfoField();
	  if (name.equals("ID")) return new IdField("[A-Z]\\d{8}", true);
	  if (name.equals("X")) return new MyCrossField();
	  if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

	//make SRC append with " " and give vali pattern
	private class MySourceField extends SourceField {
	  public MySourceField() {
	    super("[A-Z]+", true);
	  }

	  @Override
	  public void parse(String field, Data data) {
	    if (field.equals(data.strSource)) return;
	    data.strSource = append(data.strSource, " ", field);
	  }
	}


	private static Pattern ADDR_CITY = Pattern.compile("(.*?),([A-Z]*)");
	private static Pattern SORTER = Pattern.compile("(?:AREA (.*)|(?:APT|LOT|RM|ROOM|UNIT) *(.*)|#?(\\d+[A-Z]?)|(ST[AN] [^ ]+)|(?:NORTH|SOUTH|EAST|WEST)BOUND)", Pattern.CASE_INSENSITIVE);
  //field is ADDR(;(MAP|APT|UNIT|[NSEW]BOUND|PLACE))+,CITY
	private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      //remove leading empty fields ( 2 occurrences in pages )
      while (field.startsWith(";")) field = field.substring(1).trim();

      //parse ADDR and CITY
      Matcher mat = ADDR_CITY.matcher(field);
      if (!mat.matches()) abort();
      String[] fields = mat.group(1).split(" *; *");

      //ADDR
      super.parse(fields[0], data);

      //CITY
      data.strCity = convertCodes(mat.group(2).trim(), CITY_CODES);

      //parse intermediate fields
      for (int i = 1; i < fields.length; i++) {
        Matcher imat = SORTER.matcher(fields[i]);
        //if no match, append to PLACE
        if (!imat.matches()) { data.strPlace = append(data.strPlace, ", ", fields[i]); continue; }
        //MAP
        String group = imat.group(1);
        if (group != null) { data.strMap = append(data.strMap, ", ", group); continue; }
        //APT
        group = imat.group(2);
        if (group == null) group = imat.group(3);
        if (group != null) { data.strApt = append(data.strApt, "-", group); continue; }

        //UNIT
        group = imat.group(4);
        if (group != null) { data.strUnit = append(data.strUnit, " ", group.replace(" ", "")); continue; }
        //if mat matched, but no groups are captured, whole field is a direction (like SOUTHBOUND). append to ADDR
        data.strAddress = append(data.strAddress, " ", fields[i]);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " MAP PLACE CITY";
    }
  }

  private static final Pattern DT_OPERATOR = Pattern.compile("(\\d{2} \\d{2} \\d{2}) (\\d{2}/\\d{2}/\\d{4}) - .*?, .* \\(.*\\)");
  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{3,}[, ]+[-+]?\\d{2,3}\\.\\d{3,}\\b) *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      //if DT hasen't been parsed yet, check if this field is one + parse it
      Matcher mat = DT_OPERATOR.matcher(field);
      if (mat.matches()) {
        if (data.strDate.length() == 0) {
          data.strTime = mat.group(1).replace(" ", ":");
          data.strDate = mat.group(2);
        }
      } else if ((mat = GPS_PTN.matcher(field)).lookingAt()) {
        setGPSLoc(mat.group(1), data);
        field = field.substring(mat.end()).trim();
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "TIME DATE GPS "+super.getFieldNames();
    }
  }

  private static Pattern REMOVE_NOTFOUND = Pattern.compile("Between: *(?:<not found> & )?(.*?)(?: & <not found>)?");
  private class MyCrossField extends CrossField {
    public MyCrossField() {
      super("(?:Between|Intersection of):.*", true);
    }

    @Override
    public void parse(String field, Data data) {
      //if field.startswith "Intersection of:" make sure it isn't identical to the address (or a truncation), remove the tag in either case
      if (field.startsWith("I")) {
        //Intersection of: (.*)
        field = field.substring(field.indexOf(":")+1).trim();
        //make sure field isn't redundant
        if (data.strAddress.startsWith(field)) return; //don't parse, this field is redundant
      } else {
        //Between: (.*) - remove <not found>'s before parsing
        Matcher mat = REMOVE_NOTFOUND.matcher(field);
        if (!mat.matches()) abort();
        field = mat.group(1);
      }

      //assign to strCross
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}(?::\\d{2})?");
  private class MyDateTimeField extends DateTimeField {

    public MyDateTimeField() {
      super("\\d{2}/[0-9/: ]*", true);
    }

    @Override
    public void parse(String field, Data data) {

      // Skip truncated data
      if (!DATE_TIME_PTN.matcher(field).matches()) return;

      // skip parse() if DATETIME has been parsed from INFO
      if (data.strDate.length() == 0) super.parse(field, data);
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "2050 W STATE ROUTE 89A",               "+34.752797,-112.045739"
  });

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "CLA", "CLARKDALE",
      "COR", "CORNVILLE",
      "CV",  "CAMP VERDE",
      "CW",  "COTTONWOOD",
      "JER", "JEROME",
      "SED", "SEDONA",
      "RIM", "RIMROCK"
  });

}
