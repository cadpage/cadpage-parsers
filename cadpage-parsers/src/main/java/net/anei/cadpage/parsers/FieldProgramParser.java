package net.anei.cadpage.parsers;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * This is a general purpose programmable field parser.  It can be used as a
 * base class for parser that have to process an array of fields that lack
 * keyword identifiers but which should appear in a particular order.  There
 * is a provision to allow the order to be adjusted to handle optional fields
 * that may or may not be present.
 *
 * The field sequence program consists of one string with a number of field
 * terms separated by blanks.  Each field term consists of the following
 *
 * 1) A field name, these will match the names used in parser testing classes
 *    Subclasses can override the default field processors, or add their own
 *    by overriding the getField() method
 *
 * 2) An optional slash followed by one or more alphanumeric characters that
 *    can be used to designate special handling by this field processor.
 *
 * 3) An optional exclamation point [!] which indicates that this field is
 *    required and the parse operation should fail if it runs out of data before
 *    this field is processed, or
 *    An optional percent sign [%] which indicates that this field is expected.
 *    A missing expected field will not cause the parse operation to fail, but
 *    it will cause Cadpage to wait for a second text message containing the
 *    rest of the CAD page
 *
 * 4) An optional plus sign [+] which indicates that this field should repeat
 *    Indefinitely.  Typically this will only be used for INFO fields
 *
 * 5) An option question mark [?] which indicates that this field may not be
 *    present.  The question mark may or may not be followed by a trigger
 *    character.  If there is a trigger character, the presence of this character
 *    in position one of the data field indicates that the field is present, otherwise
 *    this field term will be skipped and the next field term used to process
 *    the current data field.  If there is no trigger character the program
 *    will attempt to deduce whether the field exists or not by querying the
 *    field processor for this and possibly subsequent field terms
 *
 * Field Qualifiers
 *   All conditional fields
 *     Y - Force condition approval. Reject result if field contents do not
 *         satisfy condition checks
 *     Z - suppress condition checks.  This is useful is this fields condition
 *     check is less reliable than another field behind it
 *
 *   Address fields
 *     y - parse -xx city convention
 *     i - implied intersection convention
 *     s - accept sloppy addresses
 *     a - no implied appartment (only with smart parser logic)
 *     S - Invoke smart parser logic, this is followed by some optional flag
 *         characters, followed by up two 3 field designation characters
 *         Flag characters
 *         0 - @ or AT can mark beginning of address or place
 *         1 - @ or AT marks beginning of place
 *         2 - Only @ will be treated as start maker.  Ignore "AT"
 *         3 - Address followed by cross street or something similar
 *         4 - Empty address should be accepted
 *         5 - Turn on the FLAG_CROSS_FOLLOWS even if we don't have a following
 *             cross street.  Useful to work around city names that start with N or S
 *         6 - additional checks to detect non-numeric implied apartment fields
 *         7 - There may not be a blank between start field and address :(
 *         8 - There may not be a street suffix :(:(:(
 *         9 - Do not look for city
 *         First field character determines what can come ahead of the address
 *         X - nothing
 *         C - call description (req)
 *         c - call description (opt)
 *         L - Call and Place (call req)
 *         l - Call and Place (opt)
 *         P - Place name (opt)
 *         p - place name (req)
 *         S - something we can skip (opt)
 *         s - something we can skip (req)
 *         Second field character determines what data comes after the address
 *         X - nothing
 *         C - call description
 *         P - place name
 *         S - something we can skip
 *         a - apartment
 *         U - unit
 *         N - name
 *         I - supplemental info
 *         x - cross streets
 *         Third field character determines what to do with a special field between
 *         the regular address and the city field
 *         X - nothing
 *         P - place name
 *         S - something we can skip
 *         a - apartment
 *         x - cross streets
 *
 *   Date and Date/Time fields
 *         d - replace dashes with slashes
 *
 *   Call, Info, and Unit Fields take separator qualifiers.  There can be
 *   multiple such character which when combined together build the connection
 *   string used to append multiple occurrences of this data field
 *         N - Newline
 *         C - Comma
 *         D - Dash
 *         S - Space
 *         L - Slash
 *
 *   Call, Skip, and Info fields
 *         G - Mark call as general alert
 *         R - Mark call as run report
 *
 *   GPS fields
 *         d - assume 6 implied decimal digits
 *
 * SPECIAL FIELD NAMES
 *
 * INTLS - operator initials, always skipped but will validated as 1-3 upper case letters
 *
 * EMPTY - Empty field.  Must always be empty
 *
 * ADDRCITY - Address and City field separated by a comma.  Can take all ofthe
 * special field qualifiers that an address field can.
 *
 * END - Does no processing, fails if not beyond the end of the data fields
 *
 * SELECT - Does not process a field, only makes yes/no selection decision after calling the
 *          getSelectValue() method.  If the result matches the qualifer string, the test
 *          succeeds, otherwise it fails.
 *
 * The ugly details on optional fields
 *
 * Some data fields (like ADDR, ID, and X) have internal logic
 * to make a decision as to whether a particular data field is valid or not.
 * For these, the ? qualifier just calls that fields validation logic and uses
 * that to determine whether or not the field is present.
 *
 * Others, like PLACE and INFO, have no such logic.  Yet we can and often do
 * use the ? qualifier when the field may or may not be present.  In such cases
 * the program looks for the next field after the optional field that does have
 * validation logic and tries to process that field on the assumption that the
 * optional field is missing.  If the validation fails, it proceeds assuming
 * the optional field is present.  The exact logic on which fields to check and
 * in what order is made when the parser class is instantiated, the program
 * string you pass is compiled into a set of parse and validation steps which
 * can then be executed when a string needs to be parsed.
 *
 * Conditional branches
 *
 * Conditional branches are an advanced feature that can be incredibly
 * useful in certain cases.  If there is a point in your program where
 * different field patterns may be possible, they can be enclosed in
 * parenthesis and separated by pipe characters.  For example
 *
 * CALL ADDR ( CITY STATE | MAP ) INFO+
 *
 * All of the conditional branches in a group, except the last one, must have
 * contain a validatable field that will be used to determine if this is the
 * correct branch to process
 *
 * Keyword tags
 *
 * tagged field can be defined by prefixing the field term with a tag name and
 * a colon.  A tagged field term will only match a data field that starts with
 * that tag name and a colon.  Tagged and positional (untagged) fields can
 * be intermixed.
 *
 * A keyword tagged field can be declared optional, which determines what happens
 * when no matching data field is found.  Normal behavior is to assume that
 * the tagged data field exists somewhere beyond the end of the field and pretty
 * much terminates field processing.  The the tagged field is declared optional
 * with a ? qualifier and no matching data field is found, the tagged field is
 * simply ignored and processing picks up with the next field.
 *
 * Debugging
 *
 * There are only four points where this class determines that a page fails to parse.
 * All are marked with a // BREAKPOINT comment.  Put a breakpoint on all 4 of them
 * and you can probably tell why the parse is failing in short order.
 */

public class FieldProgramParser extends SmartAddressParser {

  // Flag indicating fields can occur in any order
  // Field keywords are required to keep things straight
  public static final int FLDPROG_ANY_ORDER = 1;

  // Flag indicating that keyword matches do not have to match case
  public static final int FLDPROG_IGNORE_CASE = 2;

  // Flag indicating that double underscores are required to escape blanks in keywords
  public static final int FLDPROG_DOUBLE_UNDERSCORE = 4;

  // Flag indicating that we should parse alert message as HTML text
  public static final int FLDPROG_XML = 8;

  // Flag indicating that newline chars should be treated as field breaks in keyword delimited messages
  public static final int FLDPROG_NL_BRK = 0x10;

  // list of cities
  private Set<String> cities = null;

  // table converting city codes to city names
  private Properties cityCodes = null;

  // Start of program steps
  private StepLink startLink;

  // True if any of our program steps contain a tag
  private boolean parseTags = false;

  // True if any programs steps are empty tags
  private boolean emptyTags = false;

  // List of tags on the main path
  private String[] tagList = null;

  // Character used to terminate keywords
  private char breakChar = ':';

  // Fields can occur in any order
  private boolean anyOrder;

  // Case should be ignored when comparing keywords
  private boolean ignoreCase;

  // String sequence that escapes blanks in keywords
  private String blankEscape;

  // XML message parsing
  private boolean xml;

  // newline chars should be treated as field breaks
  private boolean newLineBrk;

  // XML parser
  SAXParser xmlParser;

  private Map<String, Step> keywordMap = null;

  // Overall parser state information
  private State state;

  // List of step fields executed in the process of parsing a text string
  // This is used by the getProgram() method to calculate a field
  // order list that can be used to generate a test for this parsing
  private Field[] fieldRecord = null;

  public static String setExpectFlag(String program, String fldTerm) {
    if (fldTerm == null) return program;
    int pt = program.indexOf(fldTerm);
    if (pt < 0) throw new RuntimeException("Field term not found");
    pt += fldTerm.length();
    return program.substring(0,pt) + '%' + program.substring(pt);
  }

  public FieldProgramParser(String[] cities, String defCity, String defState, String programStr) {
    this(cities, defCity, defState, CountryCode.US, programStr, 0);
  }

  public FieldProgramParser(String[] cities, String defCity, String defState, String programStr, int flags) {
    this(cities, defCity, defState, CountryCode.US, programStr, flags);
  }

  public FieldProgramParser(Properties cityCodes, String defCity, String defState, String programStr) {
    this(cityCodes, defCity, defState, programStr, 0);
  }

  public FieldProgramParser(Properties cityCodes, String defCity, String defState, String programStr, int flags) {
    super(cityCodes, defCity, defState);
    this.cityCodes = cityCodes;
    setProgram(programStr, flags);
  }

  public FieldProgramParser(String defCity, String defState, String programStr) {
    this(defCity, defState, CountryCode.US, programStr, 0);
  }

  public FieldProgramParser(String defCity, String defState, CountryCode country, String programStr) {
    this(defCity, defState, country, programStr, 0);
    setProgram(programStr, 0);
  }

  public FieldProgramParser(String defCity, String defState, String programStr, int flags) {
    this(defCity, defState, CountryCode.US, programStr, flags);
  }

  public FieldProgramParser(String defCity, String defState, CountryCode country, String programStr, int flags) {
    super(defCity, defState, country);
    setProgram(programStr, flags);
  }

  public FieldProgramParser(String[] cities, String defCity, String defState, CountryCode country, String programStr) {
    this(cities, defCity, defState, country, programStr, 0);
  }

  public FieldProgramParser(String[] cities, String defCity, String defState, CountryCode country,
                             String programStr, int flags) {
    super(cities, defCity, defState, country);
    if (cities != null) this.cities = new HashSet<String>(Arrays.asList(cities));;
    setProgram(programStr, flags);
  }

  @Override
  protected void setupCities(Collection<String> cities) {
    super.setupCities(cities);
    if (this.cities == null) this.cities = new HashSet<String>();
    this.cities.addAll(cities);
  }

  @Override
  protected void setupCities(Properties cityCodes) {
    super.setupCities(cityCodes);
    if (this.cityCodes == null) this.cityCodes = cityCodes;
  }

  /**
   * Set the character used to mark the end of  keyword
   * @param breakChar new break character
   */
  protected void setBreakChar(char breakChar) {
    this.breakChar = breakChar;
  }


  @Override
  public void setFieldList(String fieldList) {
    fieldRecord = null;
    super.setFieldList(fieldList);
  }

  /**
   * This is only called by the test generation logic in an attempt
   * to determine the order of fields within the text message.  Originally
   * it did this by returning the uncompiled program string.  Now
   * it tries to build this string while executing the compiled program
   * @return uncompiled program string or reasonable facimile thereof
   */
  @Override
  public String getProgram() {
    if (fieldRecord == null) return super.getProgram();
    StringBuilder sb = new StringBuilder();
    for (Field field : fieldRecord) {
      if (field != null) {
        String names = field.getFieldNames();
        if (names != null) {
          if (sb.length() > 0) sb.append(' ');
          sb.append(names);
        }
      }
    }
    return sb.toString();
  }

  /**
   * Compile program string into a Step tree that will execute the requested
   * field assignments
   * @param programStr program string to be compiled
   */
  private static final Pattern TAG_PTN = Pattern.compile("([^ :]+):");
  protected void setProgram(String program, int flags) {

    xml = (flags & FLDPROG_XML) != 0;
    anyOrder = xml || (flags & FLDPROG_ANY_ORDER) != 0;
    ignoreCase = (flags & FLDPROG_IGNORE_CASE) != 0;
    newLineBrk = (flags & FLDPROG_NL_BRK) != 0;
    blankEscape = (flags & FLDPROG_DOUBLE_UNDERSCORE) != 0 ? "__" : "_";

    if (xml) {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setValidating(false);
      try {
        xmlParser = factory.newSAXParser();
      } catch (ParserConfigurationException | SAXException ex) {
        throw new RuntimeException(ex);
      }
    }

    if (program == null) return;

    parseTags = false;

    // Construct a head link and tail node and compile the tokens between them
    this.startLink = new StepLink(0);
    Step tail = new Step();
    compile(this.startLink, program, tail, null, false);

    // Make a cleanup pass through all of the defined steps
    // removing any skip steps and
    // decrementing the data index increment for any success links for select
    // steps by one.  This is necessary because select steps do not actually
    // consume a data position
    // One last complication.  Do not remove skip steps following an optionaltagged
    // field.  They may be necessary to provide a landing space for an untagged data
    // field.
    List<Step> stepList = getAllSteps();
    initStepScan();
    for (Step step : stepList) {
      if (step.tag != null && step.optional){
        Step nextStep = step.getNextStep();
        if (nextStep != null) nextStep.markChecked();
      }
      if (!step.isChecked()) step.removeSkip();
      step.backSelectLink();
    }

    // Now that we no longer need it, reduce the tail node
    tail.removeSkip();

    // And finally build a list of tags we can use to parse undelimited messages
    // We used to do this by scanning through the compiled steps, but that misses
    // tags on conditional branches.  Now we just parse tags from the program string
    tagList = null;
    if (parseTags) {
      List<String> tags = new ArrayList<String>();
      Matcher match = TAG_PTN.matcher(program);
      while (match.find()) {
        String key = match.group(1).replace(blankEscape, " ");
        if (ignoreCase) key = key.toUpperCase();
        tags.add(key);
      }
      tagList = tags.toArray(new String[tags.size()]);
    }

    // If we are processing fields in any order, build a keyword > Field
    if (anyOrder) {
      keywordMap = new LinkedHashMap<String, Step>();
      stepScan(new StepScanListener<Map<String,Step>>(){
        @Override
        public void processStep(Step step, Map<String, Step> keywordMap) {
          if (step.field != null) {
            String tag = step.tag == null ? "" : step.tag;
            if (keywordMap.put(tag, step) != null) {
              throw new RuntimeException("Duplicate tag:" + tag + " in anyorder or xml program");
            };
          }
        }
      }, keywordMap);
    }

//    System.out.println(program);
//    System.out.println(toString());
  }

  /**
   *
   * Compile program string into a chain of program steps
   * @param headLink The step link that should end up pointing to the start
   * of the program chain
   * @param program Program string to be compiled
   * @param tail Program step were all execution chains should terminate
   * @param failStep If not null, the step that should be jumped to if
   * a data stream cannot be matched to the program.  If null, no attempt
   * will be made to validate the data stream.
   * @param nextFail true if fail step should be used as the next step link
   */
  private void compile(StepLink headLink, String program, Step tail, Step failStep, boolean nextFail) {

    // Empty string is a special, easily handled, case
    if (program.length() == 0) {
      if (failStep != null) {
        throw new RuntimeException("Deferred optional status of was not resolved by empty program");
      }
      headLink.setLink(tail);
      return;
    }

    // Split program string into field terms
    String[] fieldTerms = tokenize(program);

    // Build arrays of term info and of field steps for each field term
    FieldTermInfo[] infoList = new FieldTermInfo[fieldTerms.length];
    Step[] fieldSteps = new Step[fieldTerms.length];

    // First pass through the field terms, constructing the appropriate field
    // and program step that will be used.
    for (int ndx = 0; ndx < fieldTerms.length; ndx++) {
      FieldTermInfo info = new FieldTermInfo(fieldTerms[ndx], blankEscape);
      if (anyOrder) {
        if (info.optional) {
          throw new RuntimeException("Any order parsers do not support optional fields:" + fieldTerms[ndx]);
        }
      }
      if (info.repeat && info.optional && info.tag != null) throw new RuntimeException("Tagged optional repeat fields do not work");
      if (ignoreCase & info.tag != null) info.tag = info.tag.toUpperCase();
      infoList[ndx] = info;
      fieldSteps[ndx] = new Step(info);
    }

    // Initialize stuff, and link the heading link to the first step
    // If there is a failure step (meaning we are compiling a conditional branch)
    // There is an implicit decision required about the first node
    int optBreak = (failStep != null ? 0 : -1);
    boolean optRepeat = false;
    headLink.setLink(fieldSteps[0]);

    // Now to start a second pass, this will will figure out just how the
    // steps need to be linked together
    for (int ndx = 0; ndx < fieldTerms.length; ndx++) {

      // Get the previous link, and current, and next steps in normal sequence
      Step step = fieldSteps[ndx];
      Step next = ndx+1 < fieldTerms.length ? fieldSteps[ndx+1] : tail;

      // Check term info for things that would disrupt the natural order of
      // things
      FieldTermInfo info = infoList[ndx];

      // Of which the most disruptive by far is an untestable optional field
      // somewhere behind us.
      if (optBreak >= 0) {

        // We are looking for a normal testable field that can resolve the condition
        // status of the untestable one.  If we find repeat or optional step
        // or tagged step before that, all is lost
        if (info.repeat || info.optional) {
          throw new RuntimeException("Deferred optional status of " + fieldTerms[optBreak] +
                                      " not resolved before " + fieldTerms[ndx]);
        }

        // OK, is this our testable determination step?
        // Conditional branches are testable by definition
        if (info.branch || step.canFail()) {

          // All outgoing links will be relative to this step
          // If this is a conditional branch step, is will be split up into
          // multiple steps of unknown length.  The only way to keep the
          // data links strait is to lock the data field index to this step

          // An additional problem comes up when logic flow needs to jump
          // from the field before to the field after a decisions step.  If
          // the decision step happens to be a  conditional branch the field
          // index needs to be locked to the decision step and the actual
          // field increments needs to be one less than usual
          Step branchStep = null;
          int jumpOver = 2;
          if (info.branch) {
            branchStep = step;
            jumpOver = 1;
          }

          // There are three reasons why a condition check is deferred
          // First we could be processing a conditional branch subprogram
          int delta = ndx - optBreak;
          Step optStep = fieldSteps[optBreak];
          if (failStep != null) {

            // Start link should point to this node
            // with the appropriate index adjustment
            headLink.setLink(step, delta);

            // Failure link jumps to failure step, reversing the index adjustment
            step.getFailLink().setLink(failStep, -delta, branchStep);

            // And the forward tag search path should jump to the failure step
            // except for the last branch of a conditional step which uses the
            // normal next step link
            if (nextFail) step.setNextStep(failStep);

            // Success link can go two different ways
            // if the decision step is the first step, success links to the next step
            if (delta == 0) {
              step.getSuccLink().setLink(next, +1);
            }

            // Otherwise, success link jumps back to first step, and the step
            // before the decision step bypasses the decision step
            else {
              step.getSuccLink().setLink(fieldSteps[optBreak], -delta, branchStep);
              fieldSteps[ndx-1].getSuccLink().setLink(next, jumpOver, branchStep);
            }

            // The conditional branch decision has been made,
            // Null out the fail step so we don't try to do it again
            failStep = null;

          }
          // Or it could be terminating an earlier repeat block
          else if (optRepeat) {

            // Redirect all links to the conditional repeat node to this, decision, node
            // Assuming that the optional node field does not exist.
            // Note, this include the link originally set up from the repeat node to itself
            fieldSteps[optBreak].redirect(step, delta-1);

            // Failure link jumps back to the optional repeat node, which we now assume
            // does exist
            step.getFailLink().setLink(optStep, -(delta-1), branchStep);

            // Success link is trickier
            // If optional node and decision node are next to each other,
            // Success link just moves on the the next node normally
            if (delta == 1) {
              step.getSuccLink().setLink(next, +1);
            }

            // If there is a sequence of steps between the option and decision
            // steps success link jumps back to the first one, the success
            // link of the last one jumps over the decision step to point to
            // the following step
            else {
              step.getSuccLink().setLink(fieldSteps[optBreak+1], -(delta-1), branchStep);
              fieldSteps[ndx-1].getSuccLink().setLink(next, jumpOver, branchStep);
            }
          }

          // Otherwise this is a normal deferred conditional test
          else {

            // The decision step needs to
            // be cloned because it will have to appear twice in the step program.
            // The new step will not be a decision step and will flow
            // normally to the next step
            // Tricky note..  It is critical the the existing step be the decision step
            // because it may be used by existing relative step index links
            Step newStep = step.cloneStep();
            step.redirect(newStep, 0, null, true);
            newStep.getSuccLink().setLink(next, +1);

            // redirect all links to the optional step to the decision step
            // assuming that the optional field does not exist
            optStep.redirect(step, delta-1);

            // A step failure here implies that the optional field does exist
            // Failure branch will jump back to the optional branch, and proceed
            // normally from there, which includes processing the original step
            // for this field without a failure option
            step.getFailLink().setLink(optStep, -(delta-1), branchStep);

            // If the optional and decision steps are next to each other
            // the success branch can just move on the the next step normally
            if (delta == 1) {
              step.getSuccLink().setLink(next, +1);
            }

            // Otherwise, the success branch will execute a chain of steps cloned from all
            // of the steps between the optional step and this decision step
            // (which may be empty if the two are next to each other) then
            // skip this step and take up normal processing with the next step
            else {
              int incAdj = -(delta-1);
              Step prevStep = step;
              Step tmpBrStep = branchStep;
              for (int jj = optBreak+1; jj<ndx; jj++) {
                Step tmpStep = fieldSteps[jj].cloneStep();
                prevStep.getSuccLink().setLink(tmpStep, incAdj, tmpBrStep);
                prevStep = tmpStep;
                incAdj = 1;
                tmpBrStep = null;
              }
              prevStep.getSuccLink().setLink(next, jumpOver, branchStep);
            }

            // If this was a branch step, it needs to be compiled now because
            // it will not be found in the regular step list durring the third pass
            if (info.branch) compileBranch(newStep, fieldTerms[ndx]);
          }

          // And finally, reset the optional break so we can resume
          // normal processing
          optBreak = -1;
        }

        // Otherwise, we are still searching for a decision mode, follow
        // normal logic flow
        else {
          step.getSuccLink().setLink(next, +1);
        }
      }

      // So we aren't in deferred decision mode
      // See if this node is flagged to repeat
      else if (info.repeat) {

        // It does, normal chain links back to the same step, processing
        // Consecutive fields until something happens
        step.getSuccLink().setLink(step, +1);

        // We need to set a next link to get out of here, lest next link searches
        // wind up in an infinite loop
        step.setNextStep(next);

        // If this is an optional repeat, things get complicated
        if (info.tag == null && info.optional) {

          // If the step can detect failures, we can set the failure branch
          // to the next step, processing the same field there
          if (step.canFail()) {
            step.getFailLink().setLink(next);
          }

          // If it can't detect failures, we have to defer the decision
          // which is going to pretty much work the way the regular deferred
          // optional field words.  Although we still have the success link
          // pointing to itself, it will eventually be redirected to the
          // actual decisions step.  So it is no longer necessary to retain
          // the next step link
          else {
            optRepeat = true;
            optBreak = ndx;
            step.setNextStep(null);
          }
        }
      }

      // Next thing to check is a optional flag without the repeat flag
      else if (info.tag == null && info.optional) {

        // In any case, logic from from this step goes to the next step
        step.getSuccLink().setLink(next, +1);

        // If this step can do its own validity testing, life is wonderful
        // We just set the fail condition to process the next step with the
        // same data field
        if (step.canFail()) {
          step.getFailLink().setLink(next);
        }

        // Otherwise, life gets complicated.
        // We will have to defer the decision as to whether this field exists
        // or not based on a subsequent field that can be validated.  For now
        // just remember where the option break is
        else {
          optRepeat = false;
          optBreak = ndx;
        }
      }

      // Otherwise, there is nothing unusual or extraordinary about this step
      // It will just link to the next step and field.
      else {
        step.getSuccLink().setLink(next, +1);
      }
    }

    // End of second pass
    // If we are still trying to resolve an optional break, complain
    if (optBreak >= 0) {
      throw new RuntimeException("Deferred optional status of " + fieldTerms[optBreak] +
                                  " was never resolved");
    }

    // And now for a third pass to expand any conditional branch terms
    for (int ndx = 0; ndx < fieldTerms.length; ndx++) {
      if (infoList[ndx].branch) {
        compileBranch(fieldSteps[ndx], fieldTerms[ndx]);
      }
    }
  }

  /**
   * Compile a conditonal branch step identified in an earlier pass
   * @param branchHead Branch step that needs to be recompiled
   * @param term Field term associated with branch step
   */
  private void compileBranch(Step branchHead, String term) {

    // Split the branch step into a branch head and tail steps
    // and a separate failure link

    StepLink failLink = branchHead.failLink;
    branchHead.failLink = null;
    Step branchTail = branchHead.split();

    Step brFailStep = null;
    if (failLink != null) {
      brFailStep = new Step();
      brFailStep.succLink = failLink;
    }

    //  Next break the field term up into the different conditional branches
    String[] branchTerms = splitBranches(term);

    // Loop through each of the optional branches
    int cnt = 0;
    for (String branchTerm : branchTerms) {
      branchTerm = branchTerm.trim();

      // We need a temporary skip step with no data increment to link
      // each conditional branch chains to the next.  Except for the
      // last one which will be given the original failure link
      // The branch exist step will also use this skip step as the next step
      // link, except for the last branch which uses the normal null next step
      boolean lastStep = (++cnt == branchTerms.length);
      Step linkStep = lastStep ? brFailStep : new Step();

      // Each branch will be compiled using the branch head step as the
      // start step, the branch tail step as the tail step, and the new
      // link step as the branch failure step
      compile(branchHead.getSuccLink(), branchTerm, branchTail, linkStep, !lastStep);
      branchHead = linkStep;
    }

    // Each compile step transfered control to the tail step as though it
    // were the next step in sequence, meaning that they incremented the
    // data pointer for it.  But in fact, we want the tail step to hold
    // the index of the last data field processed.  Which means we have
    // to go through all of its in links and back the data increment by one
    // for all of them
    for (StepLink link : branchTail.getInLinks()) {
      if (link.getStep() == branchTail) link.chainLink(branchTail, -1, null);
    }
  }

  /**
   * Break program string up into an array of tokens.  Tokens are generally
   * separated by blanks, but a list of items in parenthesis will be combined
   * into a single token
   * @param programStr program string to be compiled
   * @return list of program terms from program string
   */
  private String[] tokenize(String programStr) {

    // Initialize stuff
    List<String> tokenList = new ArrayList<String>();
    StringBuilder sb = null;
    int level = 0;

    // Break string down into blank delimited tokens and run through them
    for (String token : programStr.split(" +")) {

      // If we are working at the bottom level (which most of the time we will)
      if (level == 0) {

        if (token.equals(")")) {
          throw new RuntimeException("Mismatched parens in " + programStr);
        }

        // Check for open paren.  If we find one, crate a StringBuilder and
        // save it there
        if (token.equals("(")) {
          if (anyOrder) throw new RuntimeException("Conditional branches not allowed in any order parsers");
          sb = new StringBuilder(token);
          level++;
        }

        // Otherwise add this token to the token list
        else tokenList.add(token);
      }

      // If we aren't at level zero, we are working with something in parenthesis
      else {

        // Append this token to the saved string builder
        sb.append(' ');
        sb.append(token);

        // Adjust the parenthesis nesting level
        if (token.equals("(")) level++;
        if (token.equals(")")) {
          if (--level == 0) {

            // If we have closed the last paren create a token from the
            // StringBuilder and save it in the token list
            tokenList.add(sb.toString());
            sb = null;
          }
        }
      }
    }

    // All done, but make sure we don't have any unmatched open parens
    if (level > 0) {
      throw new RuntimeException("Missing closing paren in" + programStr);
    }

    return tokenList.toArray(new String[tokenList.size()]);
  }

  /**
   * Split a conditional branch program into component branches
   * @param program conditional branch program
   * @return array of conditional branch components
   */
  private static String[] splitBranches(String program) {

    List<String> branches = new ArrayList<String>();
    int st = 1;
    int lev = 0;
    for (int pt = 1; pt<program.length(); pt++) {
      char chr = program.charAt(pt);
      if ((chr == '|' && lev == 0) || pt == program.length()-1) {
        branches.add(program.substring(st, pt).trim());
        st = pt+1;
      } else if (chr == '(') lev++;
      else if (chr == ')') lev--;
    }
    if (lev != 0) throw new RuntimeException("Mismatched () in branch token: " + program);
    return branches.toArray(new String[branches.size()]);
  }

  // Enum report required status of a program step
  // NORMAL - nothing special
  // REQUIRED - If not present, message will be rejected
  // EXPECTED - If not present, message will be accepted, but will be flagged
  // as expecting more input
  private enum EReqStatus {NORMAL, REQUIRED, EXPECTED};

  /**
   * This class holds all of the basic information parsed from a program string
   * field term
   */
  private static class FieldTermInfo {
    String tag = null;
    String name = null;
    String qual = null;
    EReqStatus required = EReqStatus.NORMAL;
    boolean repeat = false;
    boolean optional = false;
    boolean htmlTag = false;
    boolean branch = false;
    boolean emptyTag = false;
    char trigger = 0;

    FieldTermInfo(String fieldTerm, String blankEscape) {

      // If this is a conditional branch, just set the branch flag and return
      // The branch will be expanded later
      if (fieldTerm.startsWith("(")) {
        branch = true;
        return;
      }

      int st = fieldTerm.indexOf("%EMPTY");
      if (st > 0) {
        tag = fieldTerm.substring(0,st).replace(blankEscape, " ");
        emptyTag = true;
      } else {
        st = fieldTerm.indexOf(':');
        if (st > 0) tag = fieldTerm.substring(0,st).replace(blankEscape, " ");
      }
      st++;

      // parse field or tag name
      int len = fieldTerm.length();
      int pt = st;
      while (pt < len &&
             (Character.isJavaIdentifierPart(fieldTerm.charAt(pt)) || fieldTerm.charAt(pt)=='-')) pt++;

      name = fieldTerm.substring(st, pt);

      // parse field qualifier, if it exists
      if (pt >= len) return;
      char chr = fieldTerm.charAt(pt++);
      if (chr == '/') {

        st = pt;
        if (pt >= len) return;
        while (pt < len && Character.isJavaIdentifierPart(fieldTerm.charAt(pt))) pt++;
        qual = fieldTerm.substring(st, pt);
        if (pt >= len) return;
        chr = fieldTerm.charAt(pt++);
      }

      // Check for Required [!] indicator
      if (chr == '!' || chr == '%') {
        required = (chr == '!' ? EReqStatus.REQUIRED : EReqStatus.EXPECTED);
        if (pt >= len) return;
        chr = fieldTerm.charAt(pt++);
      }

      // Check for Html tag < indicator
      if (chr == '<') {
        htmlTag = true;
        if (pt >= len) return;
        chr = fieldTerm.charAt(pt++);
      }

      // Check for repeat [+] indicator
      if (chr == '+') {
        repeat = true;
        if (pt >= len) return;
        chr = fieldTerm.charAt(pt++);
      }

      // Check for optional [?] indicator
      if (chr == '?') {
        optional = true;
        if (pt >= len) return;
        trigger = fieldTerm.charAt(pt++);
      }

      else {
        throw new RuntimeException("Invalid field term: " + fieldTerm);
      }
    }
  }

  private class XMLHandler extends DefaultHandler {

    Data data;
    private List<Field> fieldList = new ArrayList<>();
    private String fieldName = null;
    private StringBuilder fieldValue = new StringBuilder();

    public XMLHandler(Data data) {
      this.data = data;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (ignoreElement(qName)) return;
      fieldName = qName;
      fieldValue.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      String value = new String(ch, start, length);
      fieldValue.append(value);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (ignoreElement(qName)) return;
      processField(fieldValue.toString());
      fieldName = null;
      fieldValue.setLength(0);
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
      if (fieldName != null) {
        processField(fieldValue.toString());
      }
      super.fatalError(ex);
    }

    private boolean ignoreElement(String name) {
      return name.equals("span");
    }

    private void processField(String value) throws SAXException {
      if (fieldName == null || fieldName.isEmpty()) return;
      Step step = keywordMap.get(fieldName);
      if (step == null) return;

      // Flag step as processed
      step.markChecked();


      // and use it to process this value
      if (step.field != null) {
        try {
          step.field.parse(decodeHtmlField(value.trim()), data);
        } catch (FieldProgramException ex) {
          throw new SAXException(ex);
        }
        fieldList.add(step.field.getProcField());
      }
    }

    public void finish() {
      fieldRecord = fieldList.toArray(new Field[0]);
    }
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // We only need to parse HTML text in XML parsing mode.  Otherwise call
    // the superclass method
    if (!xml) return super.parseHtmlMsg(subject, body, data);

    // Otherwise, we do the XML parsing here
    gpsFldCnt = 0;

    // Clear the checked flags for all steps
    initStepScan();

    // Some XML alert messages are not wrapped in a root element, which
    // is a requirement for well formed XML.  We fix that by wrapping
    // everything in our own root element
    body = "<CadpageAlert911>" + body + "</CadpageAlert911>";

    // Start the XML parser and feed it the  message body
    Reader reader = new StringReader(body);
    InputSource src = new InputSource();
    src.setCharacterStream(reader);
    XMLHandler handler = new XMLHandler(data);
    try {
      xmlParser.parse(src, handler);
    } catch (SAXException | IOException ignore) {
      // Sloppy, but truncated messsages throw this exception and we
      // want to process as much as we can
      // ignore.printStackTrace();
    } finally {
      try { reader.close(); } catch (IOException ignore) {}
    }
    handler.finish();

    // Make another pass checking that all required fields have been entered
    for (Step step : keywordMap.values()) {
      if (!step.isChecked() && step.required != EReqStatus.NORMAL) {
        if (step.required == EReqStatus.REQUIRED) {
          return false;     // BREAKPOINT
        }
        data.expectMore = true;
      }
    }
    return true;
  }

  protected void setNewLineBrk(boolean value) {
    newLineBrk = value;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // We cannot break an undelimited message into fields without tag definitions
    if (!parseTags) {
      throw new RuntimeException("FieldProgramParser cannot parse message without tag definitions");
    }

    String[] fields = parseMessageFields(body, tagList, breakChar, anyOrder, ignoreCase, newLineBrk);
    return parseFields(fields, data);
  }


  /**
   * This method is invoked to process an array of parsed fields as determined
   * by the field program passed to the constructor
   * @param fields Array of fields to be processed
   * @param minFields minimum required number of fields
   * @param data Data object to be filled
   * @return true if parsing was successful
   */
  protected boolean parseFields(String[] fields, int minFields, Data data) {
    if (fields.length < minFields) return false;
    return parseFields(fields, data);
  }

  /**
   * This method is invoked to process an array of parsed fields as determined
   * by the field program passed to the constructor
   * @param fields Array of fields to be processed
   * @param data Data object to be filled{Clinton Fire} CLIF:1600132    Enroute  , FIRE AUTOMATIC ALARM  , 110 UTICA RD LUTHERAN NURSING HOME, KIRKLAND, NY ( UTICA ST/ROBINSON RD )
   * @return true if parsing was successful
   */
  protected boolean parseFields(String[] fields, Data data) {

    // If we get here in xml parsing mode, something has gone irreversably wrong
    if (xml) throw new RuntimeException("XML parse request somehome missed the parseHTMLMsg() method");

    gpsFldCnt = 0;
    fieldRecord = new Field[fields.length];
    state = new State(fields);

    // If we are running in any field order mode, things get a lot easier
    if (anyOrder) {

      // Clear the checked flags for all steps
      initStepScan();

      // Loop through all of the fields
      boolean parenKey = breakChar == ')';
      int iStartKey = parenKey ? 1 : 0;
      int fldNdx = 0;
      for (String field : fields) {

        // Break field into keyword and value
        field = field.trim();
        String tag = "";
        String value = field;
        if (!parenKey || field.startsWith("("))  {
          int pt = field.indexOf(breakChar);
          if (pt >= 0) {
            tag = field.substring(iStartKey, pt).trim();
            if (ignoreCase) tag = tag.toUpperCase();
            value = field.substring(pt+1).trim();
          }
        }

        // Get the field associated with this keyword
        // If not found, try it as an untagged field
        // If still not found, return fail result
        Step step = keywordMap.get(tag);
        if (step == null && tag.length() > 0) {
          value = field;
          step = keywordMap.get("");
        }
        if (step == null) {
          return false;                               // BREAKPOINT
        }

        // Flag step as processed
        step.markChecked();

        // and use it to process this value
        if (step.field != null) {
          try {
            state.setIndex(fldNdx);
            step.field.parse(value, data);
          } catch (FieldProgramException ex) {
            return false;                               // BREAKPOINT
          }
          fieldRecord[fldNdx++] = step.field.getProcField();
        }
      }

      // Make another pass checking that all required fields have been entered
      for (Step step : keywordMap.values()) {
        if (!step.isChecked() && step.required != EReqStatus.NORMAL) {
          if (step.required == EReqStatus.REQUIRED) {
            return false;     // BREAKPOINT
          }
          data.expectMore = true;
        }
      }
      return true;
    }

    if (state.link(startLink)) return false;
    do {} while (!state.exec(data));
    return state.getResult();
  }

  // When executing, the parser acts as a state engine.  This class preserves the parser
  // state as that engine executes
  private class State {
    private int index = 0;
    private Step step = null ;
    private Step lastStep = null;
    private boolean result = true;
    private final String[] fields;

    public State(String[] fields) {
      this.fields = fields;
    }

    public String getField(int ndx) {
      if (ndx < 0 || ndx >= fields.length) return "";
      return fields[ndx];
    }

    public String getRelativeField(int offset) {
      return getField(index+offset).trim();
    }

    public int getFieldCount() {
      return fields.length;
    }

    public boolean isLastField(int ndx) {
      return index + ndx >= fields.length;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public Step getLastStep() {
      return lastStep;
    }

    public boolean getResult() {
      return result;
    }

    public void setResult(boolean result) {
      this.result = result;
    }

    public void setStep(Step step) {
      this.step = step;
    }

    public boolean link(StepLink link) {
      if (link == null) return true;
      Step relStep = link.getRelStep();
      if (relStep != null) index = relStep.getFieldIndex();
      index += link.getInc();
      lastStep = step;
      step =  link.getStep();
      return step == null;
    }

    public boolean exec(Data data) {
      if (!step.process(data)) return false;

      // Parse state engine has completed
      // But we still need to check for any unprocessed required fields
      while (result && step != null) {
        if (! step.checkFailure(data)) result = false;
        if (step.field != null && step.field instanceof EndField &&
            step.succLink != null) step = step.succLink.step;
        else step = step.getNextStep();
      }
      return true;
    }
  }

  // Tools to support scanning all of the steps currently part of the program tree

  // Master scan code
  private int masterScanCode = 0;

  /*
   * Initialize new step scan
   */
  private void initStepScan() {
    masterScanCode++;
  }

  /**
   * Return a list of all current steps in step program
   * @return the list of current steps
   */
  private List<Step> getAllSteps() {
    List<Step> stepList = new ArrayList<Step>();
    stepScan(new StepScanListener<List<Step>>(){
      @Override
      public void processStep(Step step, List<Step> stepList) {
        stepList.add(step);
      }
    }, stepList);
    return stepList;
  }

  /**
   * Step Scan Listener interface
   */
  private interface StepScanListener<T> {
    public void processStep(Step step, T info);
  }

  /**
   * Scan all current steps in program step tree, invoking the scan step listener for each one.
   * Listener must not do anything to change any program step links.  Otherwise results are unpredictable
   * @param listener listener to be invoked for each step
   * @param info general information object to be passed to listener
   */
  private <T> void stepScan(StepScanListener<T> listener, T info) {
    initStepScan();
    stepScan(startLink, listener, info);
  }

  private <T> void stepScan(StepLink link, StepScanListener<T> listener, T info) {
    if (link == null) return;
    Step step = link.getStep();
    if (step == null) return;

    if (step.isChecked()) return;
    step.markChecked();
    listener.processStep(step, info);
    stepScan(step.succLink, listener, info);
    stepScan(step.failLink, listener, info);
    stepScan(step.nextStepLink, listener, info);
  }

  // This class performs one program step
  private class Step {

    private String tag;
    private String name;
    private String qual;
    private char trigger;
    private EReqStatus required;
    private boolean optional;
    private boolean emptyTag;
    private boolean htmlTag;

    private Field field;
    private StepLink succLink = new StepLink(0);
    private StepLink failLink= null;
    private StepLink nextStepLink = null;

    // List of all links pointing to this step,
    // including relative field link references
    private List<StepLink> inLinks = new ArrayList<StepLink>();

    // Index of field last processed by this step
    private int fieldIndex = -1;

    /**
     * Constructor
     * @param FieldTermInfo object describing this node
     */
    public Step(FieldTermInfo info) {
      this.tag = info.tag;
      this.name = info.name;
      this.qual = info.qual;
      this.trigger = info.trigger;
      this.htmlTag = info.htmlTag;
      field = null;
      if (name != null){
        field = getField(name);
        field.setQual(qual);
        field.setTrigger(trigger);
        if (field.isHtmlTag()) this.htmlTag = true;
      }
      this.required = info.required;
      this.optional = (tag != null) && info.optional;
      this.emptyTag = info.emptyTag;

      if (tag != null) parseTags = true;
      if (emptyTag) emptyTags = true;
    }

    /**
     * Create a NOOP step that does absolutely nothing.  This is useful
     * as a temporary link point when we compile conditional branches
     */
    public Step() {
      this.tag = null;
      this.name = null;
      this.qual = null;
      this.trigger = 0;
      this.field = null;
      this.required = null;
      this.optional = false;
      this.emptyTag = false;
      this.htmlTag = false;
    }

    // integer code used to mark steps that have been processed during a full step scan
    private int scanCode = 0;

    /**
     * Determine if step has been checked during current scan
     * @return
     */
    public boolean isChecked() {
      return scanCode == masterScanCode;
    }

    /**
     * Mark step as checked during current scan
     */
    public void markChecked() {
      scanCode = masterScanCode;
    }

    /**
     * @return Link to be taken on step parse succeeds
     */
    public StepLink getSuccLink() {
      return succLink;
    }

    /**
     * @return link to be taken when step parse fails
     */
    public StepLink getFailLink() {
      if (failLink == null) failLink = new StepLink(0);
      return failLink;
    }

    /**
     * Set the next step
     * @param nextStep new next step value
     */
    public void setNextStep(Step nextStep) {
      if (nextStep != null) {
        if (nextStepLink == null) nextStepLink = new StepLink(0);
        nextStepLink.setLink(nextStep);
      } else if (nextStepLink != null) {
        nextStepLink.setLink(null);
        nextStepLink = null;
      }
    }

    /**
     * @return next step
     */
    public Step getNextStep() {
      StepLink link = (nextStepLink != null ? nextStepLink : succLink);
      if (link == null) return null;
      return link.getStep();
    }

    /**
     * @return list of all links pointing to this step
     */
    public List<StepLink> getInLinks() {
      return inLinks;
    }

    public int getFieldIndex() {
      if (fieldIndex < 0) {
        throw new RuntimeException("Relative link reference to unprocesse step");
      }
      return fieldIndex;
    }

    /**
     * @return true if step can validate its data field (ie can report parse failure)
     */
    public boolean canFail() {
      // tagged steps can always identify themselves
      // untagged steps can identify themselves if their field can
      return tag != null || field != null && field.doCanFail();
    }

    /**
     * @return a clone of the current step
     */
    public Step cloneStep() {
      Step newStep = new Step();
      newStep.tag = tag;
      newStep.name = name;
      newStep.qual = qual;
      newStep.trigger = trigger;
      newStep.field = field;
      newStep.required = required;
      newStep.optional = optional;
      newStep.nextStepLink = nextStepLink;
      newStep.emptyTag = emptyTag;
      newStep.htmlTag = htmlTag;
      return newStep;
    }

    /**
     * Split a branch step into two different steps.  The original step will
     * retain all of the incoming links, the returned step will retain the
     * original outgoing link with the data index increment reduced by one.
     * relative field positioning links will be split between the head and tail
     * nodes depending on whether they negative or postive increments
     * @return the outgoing step
     */
    public Step split() {

      // Clone this step
      Step step = cloneStep();
      step.succLink = succLink;
      succLink = new StepLink(0);

      // Check all of the incoming links.  Any field position links with
      // positive increments should be moved to the tail step.
      // We have to move the targeted links to a separate temporary list
      // because the act of changing them will remove them from the incoming
      // link list, which we are not allowed to do while we are still
      // iterating through it
      List<StepLink> tmpList = new ArrayList<StepLink>();
      for (StepLink link : inLinks) {
        if (link.getRelStep() == this && link.getInc() > 0) tmpList.add(link);
      }
      for (StepLink link : tmpList) {
        link.setLink(link.getStep(), link.getInc(), step);
      }
      return step;
    }

    /**
     * If this is a skip step, remove it from the program tree, redirecting all
     * incoming links to the next link in the chain.  Care should be taken not
     * to remove a skip link that is still in the fieldSteps array and may be
     * processed by something else
     */
    public void removeSkip() {

      // Only remove links that do nothing and make no decisions
      if (! isSkipStep() || failLink != null) return;
      if (succLink.getStep() == this) return;

      // If our success link is relative to another node
      // and there are any links to anywhere that are relative
      // to this node, than this node must be retained to save
      // the field index that will be used ty that second link
      if (succLink.getRelStep() != null) {
        for (StepLink link : inLinks) {
          if (link.getRelStep() != null) return;
        }
      }

      redirect(succLink.getStep(), succLink.getInc(), succLink.getRelStep());

      // Not really necessary, but this will clear the targets incoming links entry to
      // this step, allowing it to be GCed.
      succLink.setLink(null);

    }

    /**
     * @return true if this is a NOP skip step
     */
    public boolean isSkipStep() {
      if (tag != null) return false;
      if (field == null) return true;
      if (field.pattern != null) return false;
      return field.getClass() == SkipField.class;
    }

    /**
     * Redirect all links pointing to this step to some other step
     * possibly adjusting the data field increment
     * @param newStep New step that links should be redirected to
     * @param incAdj Data field increment adjustment
     */
    public void redirect(Step newStep, int incAdj) {
      redirect(newStep, incAdj, null, false);
    }

    /**
     * Redirect all links pointing to this step to some other step
     * possibly adjusting the data field increment
     * @param newStep New step that links should be redirected to
     * @param incAdj Data field increment adjustment
     * @Param relStep Step used to anchor the field increment
     */
    public void redirect(Step newStep, int incAdj, Step relStep) {
      redirect(newStep, incAdj, relStep, false);
    }

    /**
     * Redirect all links pointing to this step to some other step
     * possibly adjusting the data field increment
     * @param newStep New step that links should be redirected to
     * @param incAdj Data field increment adjustment
     * @Param relStep Step used to anchor the field increment
     * @Param close step is being cloned rather than removed
     */
    public void redirect(Step newStep, int incAdj, Step relStep, boolean clone) {
      while (! inLinks.isEmpty()) {
        StepLink link = inLinks.remove(0);
        boolean found = false;
        if (link.getStep() == this) {
          found = true;
          link.chainLink(newStep, incAdj, relStep);
        }
        if (link.getRelStep() == this) {
          found = true;
          if (!clone) link.chainRelLink(newStep, incAdj);
        }
        if (!found) {
          throw new RuntimeException("Misdirected incoming link");
        }
      }
    }

    public void backSelectLink() {
      if (field != null && (field instanceof SelectField)) {
        if (succLink != null) {
          succLink.chainLink(succLink.getStep(), -1, null);
        }
      }
    }

    /**
     * Execute the program starting with this step.
     * @param data Data object being set up
     * @param lastStep most recently executed step
     * @return the next link to be processed, or
     * SUCCESS to indicate a successful parse, or
     * FAILURE to indicate a parse failure
     */
    public boolean process(Data data) {

      int ndx = state.getIndex();
      Step lastStep = state.getLastStep();

      // If this is a field processing step that is not interested in html tags, skip over any in the
      // data stream.  Non-field processing steps have to be left alone because
      // they may be intermediate steps handing control to another step and we
      // do not want to confuse the final field that step is supposed to get.
      int origNdx = ndx;
      if (!htmlTag && field != null) {
        while (ndx < state.getFieldCount()) {
          String fld = state.getField(ndx);
          if (!fld.startsWith("<|") || !fld.endsWith("|>")) break;
          ndx++;
        }
      }

      // Save the processed field index
      fieldIndex = ndx;

      // Have we passed the end of the data stream
      if (ndx >= state.getFieldCount()) {

        // And this is not an end or select field (which need no data to work with)
        if (field == null || ! (field instanceof EndField || field instanceof SelectField)) {

          // Otherwise, if there is a failure link, execute it
          // Unless it points back to ourselves, which can happen if there was
          // a repeating condition SKIP step
          if (failLink != null && failLink.step != this){
            return state.link(failLink);
          }

          // Otherwise, there is no field processing associated with this step
          // and there is a success link that moves us backward through the
          // field list, then take the success link.  This is very rare, but it
          // happens when a decision making conditional branch leaves a tail
          // link node that happens to fall past the end of data
          if (field == null && succLink != null && succLink.getInc() < 0) {
            return state.link(succLink);
          }

          // Otherwise we are finished
          return true;
        }
      }

      // Check for special doNotTrim processing
      boolean doNotTrim = (field != null && field.doNotTrim());

      // Now we have to deal with any tag complications
      // Default is to process this step with this data field
      // Tag processing is suppressed for Select field steps since they really
      // do no field processing
      Step procStep = this;
      String curFld = state.getField(ndx);
      if (!doNotTrim) curFld = curFld.trim();
      if (parseTags && !(field instanceof SelectField)) {
        Step startStep = this;
        int startNdx = ndx;
        boolean parenBreak = breakChar == ')';
        int iStartKey = parenBreak ? 1 : 0;
        while (true) {

          // See if data field is tagged
          // if it is extract the tag and adjust the current field value
          procStep = startStep;
          String curTag = null;
          String curVal = null;
          if (!parenBreak || curFld.startsWith("(")) {
            int pt = curFld.indexOf(breakChar);
            if (pt >= 0) {
              curTag = curFld.substring(iStartKey, pt).trim();
              if (ignoreCase) curTag = curTag.toUpperCase();
              curVal = curFld.substring(pt+1);
              if (!doNotTrim) curVal = curVal.trim();
            }
          }

          // If this is an optional tagged step, take failure branch
          // if tags do not match, otherwise process this step
          if (tag != null && failLink != null) {
            if (! matchTag(curTag, curFld)) return state.link(failLink);
            curFld = emptyTag ? "" : curVal;
            break;
          }

          // Note.  If this is an untagged step with a failure branch, we
          // also want to take the failure branch it a field tag goes somewhere.
          // But we can't do that now because we don't know if this is a real
          // field tag or a spurious colon.

          // If data field is tagged, search the program steps for one with
          // a matching tag
          if (curTag != null || emptyTags) {
            boolean skipReq = false;
            procStep = startStep;
            while (procStep != null && !procStep.matchTag(curTag, curFld)) {
              if (procStep.required == EReqStatus.EXPECTED) {
                data.expectMore = true;
              }
              else if (procStep.required == EReqStatus.REQUIRED) {

                // Skipping over a required field is OK only if a tagged
                // decision node of a conditional branch that is not
                // the last branch
                if (procStep.tag == null || procStep.nextStepLink == null) {
                  skipReq = true;;
                }
              }
              procStep = procStep.getNextStep();
            }

            // Did we find one
            if (procStep != null) {

              // If the original step was untagged and had a failure
              // branch, we should take that failure branch now.  We
              // couldn't do this earlier because the field might have
              // had a spurious colon
              if (this.failLink != null) return state.link(this.failLink);

              // If we had to skip over a required field, return failure
              if (skipReq) {
                state.setResult(false);      // << BREAKPOINT
                return true;
              }

              // Otherwise we are ready to process this step
              curFld = procStep.emptyTag ? "" : curVal;
              break;
            }

            // If we didn't find one, and this step isn't tagged, assume that
            // this is an incidental colon and process the step normally
            procStep = startStep;
          }

          // Data field is not tagged
          // If it is the last field, check to see if it might be a truncated
          // tag for a future step.  If it is, we are finished
          if (ndx == state.getFieldCount()-1 && curFld.length() > 0) {
            Step tStep = procStep;
            while (tStep != null) {
              if (tStep.tag != null && tStep.tag.startsWith(curFld)) return true;
              tStep = tStep.getNextStep();
            }
          }

          // if the current start step is also untagged, process it normally
          if (startStep.tag == null) break;

          // Current step is tagged, current data field is untagged
          // If the current step tag matches the previous step tag we assume that
          // the untagged field inherits a tag label form a previous data field.
          if (lastStep != null && startStep.tag.equals(lastStep.tag)) break;

          // No luck there.  If the the current step is an optional tagged step, skip ahead to
          // see if we can find a untagged step to match this with data field.
          // To make life just a bit more complicated, we have to adjust the current data
          // field position if the last optional tagged step that got us to this step
          // has an increment other than one
          // And if this step is interested in html tags, we have to restore the
          // original index before we skipped over any html tags
          if (startStep.optional) {
            int inc = 00;
            Step tStep = startStep;
            do {
              StepLink link = tStep.getSuccLink();
              inc += link.getInc()-1;
              tStep = link.getStep();
            } while (tStep != null && tStep.tag != null && tStep.optional);
            if (tStep != null && tStep.tag == null) {
              if (tStep.htmlTag || tStep.field == null) ndx = origNdx;
              ndx += inc;
              curFld = state.getField(ndx).trim();
              procStep = tStep;
              break;
            }
          }

          // Still no luck, Skip this data field and go on to the next one
          if (++ndx >= state.getFieldCount()) {

            // There is no data field matching this steps tag
            // If the tagged field is not flagged as optional then the assumption
            // is that there is a matching tagged field beyond the end of this
            // this text.  Which means we have reached the end of text processing
            // and need only check if there are any required fields we haven't
            // encountered
            if (!startStep.optional) return true;

            // Otherwise, the assumption is that there is no matching
            // tagged field.  So we start the whole process all over again
            // with the next step in the chain
            ndx = startNdx;
            lastStep = startStep;
            startStep = null;
            if (succLink != null) startStep = succLink.getStep();

            // If this hits the end of the chain, make sure we didn't skip
            // any required fields
            if (startStep == null) return true;
          }
          curFld = state.getField(ndx).trim();
          continue;
        }
      }
      state.setIndex(ndx);
      state.setStep(procStep);
      return procStep.process2(curFld, data);
    }

    public boolean process2(String curFld, Data data) {

      // Get current field and state information
      int ndx = state.getIndex();
      fieldIndex = ndx;

      // Next we invoke our field object to process the current data field.
      // If there is a fail step and step is no tagged, we will ask the
      // field object to check to see if this is a valid data field before
      // parsing it.  if there is not, it will not be given that option

      // If step is tagged the fail link is only taken if a matching data field
      // is not found.  We would not be here unless the data field had a matching
      // tag, so the fail step should never be taken at this point
      boolean success = true;
      try {
        if (field != null) {
          if (tag == null && failLink != null) {
            success = field.doCheckParse(curFld, data);
          }
          else {
            field.doParse(curFld, data);
          }

          // Keep a record of which fields successfully processed
          // which data fields
          if (success && ndx < fieldRecord.length) fieldRecord[ndx] = field.getProcField();

          // Nice debug info
          // System.out.println(name + ':' + success + ':' + curFld);
        }
      } catch (FieldProgramException ex) {
        state.setResult(false);
        return true;
      }

      // Jump to the next step
      return state.link(success ? succLink : failLink);
    }

    /**
     * Determine if tagged node matches field information
     * @param tag field tag information if it exists, null otherwise
     * @param field full contents of data field
     * @return true if node tag matches this information, false otherwise
     */
    public boolean matchTag(String tag, String field) {
      if (this.tag == null) return false;
      if (this.emptyTag) return this.tag.equals(field);
      return tag != null && this.tag.equals(tag);
    }

    /**
     * This is called for a process step beyond the end of the data fields
     * it returns a false status if it is a require process step
     * @return true if this is not a required field step
     */
    public boolean checkFailure(Data data) {

      // Very special case.  A required field that is the decision step of
      // a new conditional branch is not really required.
      if (tag != null && nextStepLink != null) return true;

      // If this is a required step, return failure
      if (required == EReqStatus.REQUIRED) {
        return false;      // << BREAKPOINT
      }
      if (required == EReqStatus.EXPECTED) data.expectMore = true;
      return true;
    }

    public String toString(Map<Step,Integer> stepMap) {
      StringBuilder sb = new StringBuilder();
      sb.append(getQualName());
      sb.append(getLinkName("  S:", succLink, stepMap));
      sb.append(getLinkName("  F:", failLink, stepMap));
      sb.append(getLinkName("  N:", nextStepLink, stepMap));
      return sb.toString();
    }

    private String getQualName() {
      StringBuilder sb = new StringBuilder();
      if (tag != null) {
        sb.append(tag);
        sb.append(':');
      }
      sb.append(name != null ? name : "-----");
      if (qual != null) {
        sb.append('/');
        sb.append(qual);
      }
      if (trigger != 0) {
        sb.append('?');
        sb.append(trigger);
      }
      else if (optional) {
        sb.append('?');
      }
      else if (required == EReqStatus.REQUIRED) {
        sb.append('!');
      }
      else if (required == EReqStatus.EXPECTED) {
        sb.append('%');
      }
      return sb.toString();
    }

    private String getLinkName(String prefix, StepLink link, Map<Step,Integer> stepMap) {
      if (link == null) return "";
      return prefix + link.toString(stepMap);
    }
  }

  /**
   * This class contains the information needed to link a Step to another Step
   */
  private static class StepLink {
    private Step step;     // Next step to process
    private int inc;       // Data field increment
    private Step relStep;  // If not null, data increment is relative to the data field processed by this step

    public StepLink(int inc) {
      this.step = null;
      this.inc = inc;
    }

    public Step getStep() {
      return step;
    }

    public int getInc() {
      return inc;
    }

    public Step getRelStep() {
      return relStep;
    }

    public void setLink(Step step) {
      setLink(step, 0, null);
    }

    public void setLink(Step step, int incAdj) {
      setLink(step, incAdj, null);
    }

    public void setLink(Step step, int incAdj, Step relStep) {

      if (this.step != step) {
        if (this.step != null) this.step.getInLinks().remove(this);
        this.step = step;
        if (step != null) step.getInLinks().add(this);
      }

      if (this.relStep != relStep) {
        if (this.relStep != null) this.relStep.getInLinks().remove(this);
        this.relStep = relStep;
        if (relStep != null) relStep.getInLinks().add(this);
      }

      this.inc = incAdj;
    }

    /**
     * Adjust link from one target to another, possibly adjusting the data index
     * as we do
     * @param step new target step
     * @param inc relative change in data index
     * @param relStep data index change relative to this step
     */
    public void chainLink(Step step, int inc, Step relStep) {

      // If the chained link has a relative to step, it completely
      // replaces the previous data index fields.  Otherwise we retain
      // the previous relative to step (which may be null) and add the
      // two link increments together
      if (relStep == null) {
        inc += this.inc;
        relStep = this.relStep;
      }

      // Apply the new fields to this link
      setLink(step, inc, relStep);
    }

    /**
     * Adjust link when the relative to step is dropped as an unneeded skip step.  The relative
     * to step will be moved to the target of the dropped steps outgoing link and various
     * increment adjustments will be made
     * @param step new target step
     * @param inc relative change in data index
     */
    public void chainRelLink(Step step, int inc) {
      setLink(this.step, this.inc-inc, step);
    }

    public String toString(Map<Step,Integer> stepMap) {
      StringBuilder sb = new StringBuilder();
      sb.append(inc);
      if (relStep != null) {
        sb.append('(');
        sb.append(stepMap.get(relStep));
        sb.append(')');
      }
      sb.append('/');
      if (step == null) {
        sb.append("----");
      } else {
        sb.append(stepMap.get(step));
      }
      return sb.toString();
    }

  }

  /*
   * This is the base class for all single field processors
   */
  abstract public class Field {

    private String qual = null;

    private boolean noVerify;
    private boolean forceVerify;

    private char trigger = 0;

    private Pattern pattern = null;
    private boolean hardPattern = false;

    // default constructor
    public Field() {}

    public Field(String pattern) {
      setPattern(pattern);
    }

    public Field(String pattern, boolean hardPattern) {
      setPattern(pattern, hardPattern);
    }

    public Field(Pattern pattern) {
      setPattern(pattern);
    }

    public Field(Pattern pattern, boolean hardPattern) {
      setPattern(pattern, hardPattern);
    }

    public void setTrigger(char trigger) {
      this.trigger = trigger;
    }

    public void setQual(String qual) {
      this.qual = qual;
      if (qual != null) {
        this.noVerify = qual.contains("Z");
        this.forceVerify = qual.contains("Y");
      }
    }

    public String getQual() {
      return qual;
    }

    public void setPattern(String pattern) {
      Pattern ptn = (pattern == null ? null : Pattern.compile(pattern));
      setPattern(ptn);
    }

    public void setPattern(Pattern pattern) {
      setPattern(pattern, false);
    }

    public void setPattern(String pattern, boolean hardPattern) {
      setPattern(Pattern.compile(pattern), hardPattern);
    }

    public void setPattern(Pattern pattern, boolean hardPattern) {
      this.pattern = pattern;
      this.hardPattern = hardPattern;
    }

    protected boolean isNoVerify() {
      return noVerify;
    }

    public boolean isHtmlTag() {
      return false;
    }

    /**
     * @return true if field parser has the ability to confirm whether passed
     * field is valid
     */
    public final boolean doCanFail() {
      if (noVerify) return false;
      if (pattern != null) return true;
      if (trigger != 0) return true;
      return canFail();
    }

    /**
     * @return true if field parser has the ability to confirm whether passed
     * field is valid
     */
    public boolean canFail() {
      return false;
    }

    /**
     * Check if field is valid for this position, and if it is parse it.  This
     * should never be called unless canFail() returns true
     * @param field field to be checked and parsed
     * @return if this is a valid field
     */
    public final boolean doCheckParse(String field, Data data) {

      // If there is a trigger character, see if this starts with it
      if (trigger != 0) {
        if (field.length() == 0) return false;
        if (field.charAt(0) != trigger) return false;
        parse(field.substring(1).trim(), data);
        return true;
      }
      if (pattern != null) {
        field = checkPattern(field);
        if (field == null) return false;
        parse(field, data);
        return true;
      }
      return checkParse(field, data);
    }

    /**
     * Check if field is valid for this position, and if it is parse it.  This
     * should never be called unless canFail() returns true
     * @param field field to be checked and parsed
     * @return if this is a valid field
     */
    public boolean checkParse(String field, Data data) {
      parse(field, data);
      return true;
    }

    /**
     * Perform field parsing
     * @param field field to checked and parsed
     * @param data data object to be filled in
     */
    public void doParse(String field, Data data) {

      // If a hard pattern is specified, and this doesn't pass it
      // reject this message
      if (pattern != null) {
        String tmp = checkPattern(field);
        if (tmp == null) {
          if (hardPattern) abort();
          tmp = field;
        }
        field = tmp;
      }
      if (forceVerify) {
        if (!checkParse(field, data)) abort();
      } else {
        parse(field, data);
      }
    }

    /**
     * Check if field matches pattern criteria
     * @param field field value
     * @return adjusted field value if match succeeds, null if match fails
     */
    private String checkPattern(String field) {
      if (pattern == null) return field;
      Matcher match = pattern.matcher(field);
      if (!match.matches()) return null;
      for (int ii = 0; ii < match.groupCount(); ii++) {
        String result = match.group(ii+1);
        if (result != null) return result;
      }
      return field;
    }

    /*
     * parse data field
     */
    abstract public void parse(String field, Data data);

    /**
     * Retrieve a data field before or after the current field
     * @param ndx Relative index of desired field.  0 retrieves this field,
     * positive values retrieve following fields, negative values retrieve
     * preceding fields
     * @return value of requested field if it exists, empty string if it does not
     */
    protected String getRelativeField(int ndx) {
      if (xml) throw new RuntimeException("getRelativeField() cannot be called in XML parse mode");
      return state.getRelativeField(ndx);
    }

    /**
     * @return true if we are processing the last field
     */
    protected boolean isLastField() {
      return isLastField(+1);
    }

    /**
     * @param ndx
     * @return true if we are ndx fields from the end of the message
     */
    protected boolean isLastField(int ndx) {
      if (xml) throw new RuntimeException("isLastField() cannot be called in XML parse mode");
      return state.isLastField(ndx);
    }

    /**
     * Abort field program processing and return parse failure
     */
    protected void abort() {
      throw new FieldProgramException();                // << BREAKPOINT
    }

    /**
     * @return blank separated names of the base info fields that might be set by this field
     */
    abstract public String getFieldNames();

    /**
     * @return true if data fields should not be trimmed before being processed.
     */
    public boolean doNotTrim() {
      return false;
    }

    /**
     * @return field that really processed data
     */
    public Field getProcField() {
      return this;
    }
  }

  /**
   * Call field processor
   */
  public class CallField extends Field {

    private String connector = " / ";
    boolean genAlert = false;
    boolean runReport = false;

    public CallField() {};
    public CallField(String pattern) {
      super(pattern);
    }
    public CallField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      connector = buildConnector(qual, " / ");
      if (qual != null) {
        genAlert = qual.contains("G");
        runReport = qual.contains("R");
      }
    }

    @Override
    public void parse(String field, Data data) {
      if (genAlert) data.msgType = MsgType.GEN_ALERT;
      if (runReport) data.msgType = MsgType.RUN_REPORT;
      data.strCall = append(data.strCall, connector , field);
    }

    @Override
    public String getFieldNames() {
      return "CALL";
    }
  }

  /**
   * Place field processor
   */
  public class PlaceField extends Field {

    public PlaceField() {};
    public PlaceField(String pattern) {
      super(pattern);
    }
    public PlaceField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, " - ", cleanWirelessCarrier(field));
    }

    @Override
    public String getFieldNames() {
      return "PLACE";
    }
  }

  /**
   * Address field processor
   */
  private static final Pattern SLOPPY_ADDR_PTN = Pattern.compile("\\d.*|.*[/&].*");
  private static final Pattern GPS_COMPONENT_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{5,}\\)?");
  private static final Pattern ADDR_AT_PTN = Pattern.compile(" at |@", Pattern.CASE_INSENSITIVE);
  public class AddressField extends Field {

    private boolean incCity = false;
    private boolean sloppy = false;

    // Smart address parser info
    private StartType startType = null;
    private String startField = null;
    private int parseFlags = 0;
    private String tailField = null;
    private Field tailData = null;
    private String padField = null;
    private Field padData = null;

    private boolean noCity = false;

    public AddressField() {}

    public AddressField(String ptn) {
      super(ptn, true);
    }

    public AddressField(String ptn, boolean hard) {
      super(ptn, hard);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      if (qual == null) return;

      String smartQual = null;
      int pt = qual.indexOf('S');
      if (pt >= 0) {
        smartQual = qual.substring(pt+1);
        qual = qual.substring(0,pt).trim();
      }

      incCity = qual.contains("y");
      sloppy  = qual.contains("s");
      if (qual.contains("i")) {
        startType = StartType.START_ADDR;
        parseFlags = FLAG_ANCHOR_END | FLAG_IMPLIED_INTERSECT;
      }
      if (qual.contains("a")) parseFlags |= FLAG_NO_IMPLIED_APT;

      if (smartQual == null) return;
      startType = StartType.START_ADDR;
      parseFlags |= FLAG_ANCHOR_END;
      parseFlags |= getExtraParseAddressFlags();
      boolean addPlace = false;
      pt = 0;
      while (true) {
        if (pt >= smartQual.length()) break;
        char chr = smartQual.charAt(pt);
        if (!Character.isDigit(chr)) break;
        switch (chr-'0') {
        case 0:
          parseFlags |= FLAG_AT_BOTH;
          addPlace = true;
          break;

        case 1:
          parseFlags |= FLAG_AT_PLACE;
          break;

        case 2:
          parseFlags |= FLAG_AT_SIGN_ONLY;
          break;

        case 3:
          parseFlags |= FLAG_CROSS_FOLLOWS;
          break;

        case 4:
          parseFlags |= FLAG_EMPTY_ADDR_OK;
          break;

        case 5:
          parseFlags |= FLAG_CROSS_FOLLOWS;
          break;

       case 6:
          parseFlags |= FLAG_RECHECK_APT;
          break;

        case 7:
          parseFlags |= FLAG_START_FLD_NO_DELIM;
          break;

        case 8:
          parseFlags |= FLAG_OPT_STREET_SFX;
          break;

        case 9:
          parseFlags |= FLAG_NO_CITY;
          break;
        }

        pt++;
      }

      do {
        if (pt >= smartQual.length()) break;
        char chr = smartQual.charAt(pt);
        int pt2 = "cPslCpSL".indexOf(chr);
        if (pt2 >= 0) {
          if (pt2 >= 4) {
            pt2 -= 4;
            parseFlags |= FLAG_START_FLD_REQ;
          }
          startField = new String[]{"CALL","PLACE",null,"CALL PLACE"}[pt2];
          startType = new StartType[]{StartType.START_CALL,StartType.START_PLACE,StartType.START_OTHER,StartType.START_CALL_PLACE}[pt2];
        }

        if (++pt >= smartQual.length()) break;
        chr = smartQual.charAt(pt);
        pt2 = "CPSaUNIx".indexOf(chr);
        if (pt2 >= 0) {
          parseFlags &= ~FLAG_ANCHOR_END;
          if (chr == 'x') parseFlags |= FLAG_CROSS_FOLLOWS;
          tailField = new String[]{"CALL","PLACE","SKIP","APT","UNIT","NAME","INFO","X"}[pt2];
          tailData = getField(tailField);
          if (tailField.equals("APT")) tailData.setQual("S");
          if (chr == 'I') parseFlags |= FLAG_IGNORE_AT;
        }

        if (++pt >= smartQual.length()) break;
        chr = smartQual.charAt(pt);
        pt2 = "PSax".indexOf(chr);
        if (pt2 >= 0) {
          parseFlags |= (chr == 'P' || chr == 'x' ? FLAG_PAD_FIELD : FLAG_PAD_FIELD_EXCL_CITY);
          if (chr == 'x') parseFlags |= FLAG_CROSS_FOLLOWS;
          padField = new String[]{"PLACE","SKIP", "APT", "X"}[pt2];
          padData = getField(padField);
          if (padField.equals("APT")) padData.setQual("S");
        }
      } while (false);

      if (addPlace) {
        if (tailField == null) tailField = "PLACE";
        else tailField = "PLACE " + tailField;
      }
    }

    protected void setNoCity(boolean noCity) {
      this.noCity = noCity;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // If we accept sloppy address, then anything staring with a numeric field
      // or that contains a / or an & counts
      if (sloppy && SLOPPY_ADDR_PTN.matcher(field).matches()) {
        parse(field, data);
      }

      // If we aren't using the smart parser, just call check address and parse the field
      else if (startType == null) {
        if (! isValidAddress(field)) return false;
        parse(field, data);
      }

      // If smart parser is being used, invoke it to parse the field, adding
      // FLAG_CHECK_STATUS to make sure we try to validate the field result
      else {
        int flags = parseFlags | FLAG_CHECK_STATUS;
        if (noCity) flags |= FLAG_NO_CITY;
        Result res = parseAddress(startType, flags, field);
        if (!res.isValid()) return false;

        // Looks good, lets parse out the data
        boolean parseCity = data.strCity.length() == 0;
        res.getData(data);
        parseCity = parseCity && data.strCity.length() > 0;
        fixEmptyAddress(data);
        if (padData != null) padData.parse(parseTrailingField(res.getPadField(), padData instanceof AptField, data), data);
        if (tailData != null) {
          String left = res.getLeft();
          if (!parseCity) left = parseTrailingField(left, tailData instanceof AptField, data);
          tailData.parse(left, data);
        }
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (startType == null) {
        if (incCity) {
          parseAddressCity(field, data);
          if (cityCodes != null) data.strCity = convertCodes(data.strCity, cityCodes);
        } else {
          parseAddress(field, data);
        }
      } else {
        int flags = parseFlags;
        if (data.strCity.length() > 0 || noCity) flags |= FLAG_NO_CITY;
        boolean parseCity = data.strCity.length() == 0;
        parseAddress(startType, flags, field, data);
        parseCity = parseCity && data.strCity.length() > 0;
        fixEmptyAddress(data);
        if (padData != null) padData.parse(parseTrailingField(getPadField(), padData instanceof AptField, data), data);
        if (tailData != null) {
          String left = getLeft();
          if (!parseCity) left = parseTrailingField(left, tailData instanceof AptField, data);
          tailData.parse(left, data);
        }
      }
    }

    protected boolean isValidCity(String fld) {
      if (GPS_COMPONENT_PTN.matcher(fld).matches()) return false;
      return (startType == null || startType == StartType.START_ADDR || !ADDR_AT_PTN.matcher(fld).find());
    }

    private void fixEmptyAddress(Data data) {
      if (data.strAddress.length() > 0) return;
      if ((parseFlags & FLAG_START_FLD_REQ) != 0 && startType != StartType.START_CALL_PLACE) return;
      String addr;
      switch (startType) {
      case START_PLACE:
      case START_CALL_PLACE:
        addr = data.strPlace;
        data.strPlace = "";
        break;

     case START_CALL:
        addr = data.strCall;
        data.strCall = "";
        break;

     default:
       addr = null;
      }
      if (addr != null) parseAddress(addr, data);
    }

    @Override
    public String getFieldNames() {
      StringBuilder sb = new StringBuilder("ALERT?");
      if (startType == null) {
        sb.append(" ADDR APT");
        if (incCity) sb.append(" CITY");
      }

      else {
        if (startField != null) {
          sb.append(' ');
          sb.append(startField);
        }
        sb.append(" ADDR APT PLACE? X? ");
        if (padField != null) {
          sb.append(padField);
          sb.append(' ');
        }
        sb.append("CITY ");
        if (tailField != null) sb.append(tailField);
      }
      return sb.toString().trim();
    }
  }

  /**
   * Get any extra smart address parser flags that could not be passed as address field
   * qualifiers for some reason
   * @return extra smart address paser flags
   */
  protected int getExtraParseAddressFlags() {
    return 0;
  }

  /**
   * City field processor
   */
  public class CityField extends Field {

    public CityField() {};
    public CityField(String pattern) {
      super(pattern);
    }
    public CityField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public boolean canFail() {
      return cities != null || cityCodes != null;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (cityCodes != null) {
        String city = cityCodes.getProperty(field);
        if (city != null) {
          data.strCity = city;
          return true;
        }
        if (cities == null) return false;
      }

      // Otherwise we must have a cities list
      if (!cities.contains(field.toUpperCase())) return false;
      data.strCity = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (cityCodes != null) {
        data.strCity = convertCodes(field, cityCodes);
      } else {
        data.strCity = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY";
    }
  }

  private static final Pattern ZIP_PATTERN = Pattern.compile("\\d{5}|");
  public class ZipField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (! ZIP_PATTERN.matcher(field).matches()) return false;
      if (data.strCity.length() == 0) data.strCity = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY";
    }
  }

  /**
   * Field containing address and city separated by a comma
   */
  public class AddressCityField extends AddressField {

    public AddressCityField() {};
    public AddressCityField(String pattern) {
      super(pattern);
    }
    public AddressCityField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    private Field cityField = new CityField();

    @Override
    public boolean checkParse(String field, Data data) {
      return parse(field, data, false);
    }

    @Override
    public void parse(String field, Data data) {
      parse(field, data, true);
    }

    private boolean parse(String field, Data data, boolean force) {

      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        if (isValidCity(city)) {
          cityField.parse(city, data);
          field = field.substring(0,pt).trim();
        }
      }
      else if (!force) return false;

      super.parse(field, data);
      return true;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  /**
   * Field containing address, city and optional state/zip code
   */
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(?:(\\d{5})(?:-(?:\\d{4})?)?|0))?|(\\d{5})(?:-(?:\\d{4})?)?");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern TRAIL_ZIP_PTN = Pattern.compile("(.*?) +\\d{5}(?:-(?:\\d{4})?)?");
  public class AddressCityStateField extends AddressField {

    private Field cityField = new CityField();

    public AddressCityStateField() {};
    public AddressCityStateField(String pattern) {
      super(pattern);
    }

    public AddressCityStateField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (isValidCity(city)) {
        Matcher match = ST_ZIP_PTN.matcher(city);
        if (match.matches()) {
          String state = match.group(1);
          if (state != null) {
            data.strState = state;
            zip = match.group(2);
          } else {
            zip = match.group(3);
          }
          city = p.getLastOptional(',');
          if (data.strState.isEmpty() && zip != null) {
            match = STATE_PTN.matcher(city);
            if (match.matches()) {
              data.strState = city;
              city = p.getLastOptional(',');
            }
          }
        }
        else if ((match = TRAIL_ZIP_PTN.matcher(city)).matches()) {
          city = match.group(1);
        }
        cityField.parse(city, data);
        field = p.get();
      }
      parseAddress(field, data);
      if (data.strCity.isEmpty() && zip != null) data.strCity = zip;
    }

    protected void parseAddress(String field, Data data) {
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return getAddressFieldNames() + " CITY ST";
    }

    protected String getAddressFieldNames() {
      return super.getFieldNames();
    }

  }

  /**
   * Apartment field processor
   */
  public class AptField extends Field {

    private String append = "-";

    public AptField() {};
    public AptField(String pattern) {
      super(pattern);
    }
    public AptField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, "-");
    }

    @Override
    public void parse(String field, Data data) {
      data.strApt = append(data.strApt, append, field);
    }

    @Override
    public String getFieldNames() {
      return "APT";
    }
  }

  /**
   * Building field processor
   */
  public class BuildingField extends Field {

    public BuildingField() {};
    public BuildingField(String pattern) {
      super(pattern);
    }
    public BuildingField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strApt = append(data.strApt, " ", "Bldg " + field);
    }

    @Override
    public String getFieldNames() {
      return "APT";
    }
  }

  /**
   * Parse the data field following the address that should be considered as an address
   * @param field trailing data field
   * @param data parsed data object
   */
  private String parseTrailingField(String field, boolean aptField, Data data) {

    Matcher match = SPEC_APT_INTERSECT_PTN.matcher(field);
    if (match.matches()) {
      data.strAddress = append(data.strAddress, " & ", match.group(2));
      return getOptGroup(match.group(1));
    }

    if (APT_ADDR_PTN.matcher(field).matches()) {
      data.strAddress = append(data.strAddress, " ", field);
      return "";
    }

    if (aptField && isNotExtraApt(field)) {
      data.strAddress = append(data.strAddress, " ", field);
      return "";
    }

    field = stripFieldStart(field, "/");
    field = stripFieldStart(field, "&");

    return field;
  }
  private static final Pattern SPEC_APT_INTERSECT_PTN = Pattern.compile("(?!\\d/\\d$|1/2\\b)(?:([A-Z0-9]{1,3}) *)?(?:[&/]|\\bAND\\b|\\bOFF\\b) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_ADDR_PTN = Pattern.compile("(?:NORTH|SOUTH|EAST|WEST) OF .*");

  /**
   * Special Apartment field processor
   */
  public class Apt2Field extends AptField {

    private int lengthLimit;

    public Apt2Field() {
      this(-1);
    }

    public Apt2Field(int lengthLimit) {
      this.lengthLimit = lengthLimit;
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = APT2_PTN.matcher(field);
      if (!match.matches()) {
        if (lengthLimit < 0 || field.length() > lengthLimit) return false;
      } else {
        String tmp = match.group(1);
        if (tmp != null) field = tmp;
      }
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT2_PTN.matcher(field);
      if (match.matches()) {
        String tmp = match.group(1);
        if (tmp != null) field = tmp;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT";
    }
  }
  private static final Pattern APT2_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE) *(.*)|(?:BLDG?|LOT).*");

  /**
   * Cross street field processor
   */
  public class CrossField extends Field {

    private boolean incCity = false;

    public CrossField() {}

    public CrossField(String pattern) {
      super(pattern);
    }

    public CrossField(String pattern, boolean hard) {
      super(pattern, hard);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      incCity = qual != null && qual.contains("c");
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isValidCrossStreet(field)) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (incCity) {
        int pt = field.indexOf('-');
        if (pt >= 0) field = field.substring(0,pt).trim();
      }
      data.strCross = append(data.strCross, " & ", field);
    }

    @Override
    public String getFieldNames() {
      return "X";
    }
  }

  /**
   * Box field processor
   */
  public class BoxField extends Field {

    private String append = null;

    public BoxField() {};
    public BoxField(String pattern) {
      super(pattern);
    }
    public BoxField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strBox = append(data.strBox, append, field);
      } else {
        data.strBox = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "BOX";
    }
  }

  /**
   * Unit field processor
   */
  public class UnitField extends Field {

    private String append = null;

    public UnitField() {};
    public UnitField(String pattern) {
      super(pattern);
    }

    public UnitField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    public UnitField(Pattern pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strUnit = append(data.strUnit, append, field);
      } else {
        data.strUnit = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "UNIT";
    }
  }

  /**
   * State field processor
   */
  public class StateField extends Field {

    public StateField() {
      super("[A-Z]{2}|", true);
    };
    public StateField(String pattern) {
      super(pattern);
    }
    public StateField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void parse(String field, Data data) {
      data.strState = field;
    }

    @Override
    public String getFieldNames() {
      return "ST";
    }
  }

  /**
   * Map field processor
   */
  public class MapField extends Field {

    private String append = "-";

    public MapField() {};
    public MapField(String pattern) {
      super(pattern);
    }
    public MapField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, "-");
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strMap = append(data.strMap, append, field);
      } else {
        data.strMap = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "MAP";
    }
  }

  /**
   * ID field processor
   */
  public class IdField extends Field {

    private String append = null;

    public IdField() {}

    public IdField(String pattern) {
      super(pattern);
    }

    public IdField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    public IdField(Pattern pattern) {
      super(pattern);
    }

    public IdField(Pattern pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strCallId = append(data.strCallId, append, field);
      } else {
        data.strCallId = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "ID";
    }
  }

  /**
   * Phone field processor
   */
  public class PhoneField extends Field {

    private String append = null;

    public PhoneField() {
      setPattern(Pattern.compile("\\d{10}|\\d{3}-\\d{3}-\\d{4}"));
    }

    public PhoneField(String pattern) {
      super(pattern);
    }

    public PhoneField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strPhone = append(data.strPhone, append, field);
      } else {
        data.strPhone = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PHONE";
    }
  }

  /**
   * Supplemental info field processor
   */
  static final Pattern APT_PAT = Pattern.compile("^APT( |:|#) *+(?!FIRE)", Pattern.CASE_INSENSITIVE);
  public class InfoField extends Field {

    private String connector = " / ";

    private boolean genAlert = false;
    private boolean runReport = false;

    public InfoField() {}

    public InfoField(String pattern) {
      super(pattern);
    }

    public InfoField(String pattern, boolean hard) {
      super(pattern, hard);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      connector = buildConnector(qual, " / ");
      if (qual != null) {
        genAlert = qual.contains("G");
        runReport = qual.contains("R");
      }
    }

    @Override
    public void parse(String field, Data data) {

      if (runReport) data.msgType = MsgType.RUN_REPORT;
      else if (genAlert) data.msgType = MsgType.GEN_ALERT;

      // Some special keywords will divert info to other fields
      if (field.length() <= 10 && data.strApt.length() == 0) {
        Matcher match = APT_PAT.matcher(field);
        if (match.find()) {
          data.strApt = field.substring(match.end());
          return;
        }
      }
      data.strSupp = append(data.strSupp, connector, field);
    }

    @Override
    public String getFieldNames() {
      return "INFO APT?";
    }
  }

  /**
   * Unknown field processor
   */
  public class UnknownField extends Field {

    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, " / ", field);
    }

    @Override
    public String getFieldNames() {
      return null;
    }
  }

  public class LabelInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strSupp = append(data.strSupp, "\n", getRelativeField(0));
    }

    @Override
    public String getFieldNames() {
      return "INFO";
    }
  }

  /**
   * Source field processor
   */
  public class SourceField extends Field {

    public SourceField() {};

    private String connector = null;

    public SourceField(String pattern) {
      super(pattern);
    }

    public SourceField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      connector = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (connector != null) {
        data.strSource = append(data.strSource, connector, field);
      } else {
        data.strSource = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC";
    }
  }

  /**
   * Code field processor
   */
  public class CodeField extends Field {

    private String connector = null;

    public CodeField() {};
    public CodeField(String pattern) {
      super(pattern);
    }
    public CodeField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      connector = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (connector != null) {
        data.strCode = append(data.strCode, connector, field);
      } else {
        data.strCode = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE";
    }
  }

  /**
   * Name field processor
   */
  public class NameField extends Field {

    private String connector = null;

    public NameField() {};
    public NameField(String pattern) {
      super(pattern);
    }
    public NameField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      connector = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      field = cleanWirelessCarrier(field);
      if (connector != null) {
        data.strName = append(data.strName, connector, field);
      } else {
        data.strName = field;
      }
   }

    @Override
    public String getFieldNames() {
      return "NAME";
    }
  }

  /**
   * Place/Name field processor
   * Handles a field that could be either a pesonal or public place name
   */
  public class PlaceNameField extends Field {

    public PlaceNameField() {};
    public PlaceNameField(String pattern) {
      super(pattern);
    }
    public PlaceNameField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void parse(String field, Data data) {
      field = cleanWirelessCarrier(field);
      if (checkPlace(field)) data.strPlace = field;
      else data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PLACE";
    }
  }

  /**
   * Priority field processor
   */
  public class PriorityField extends Field {

    private String append = null;

    public PriorityField() {};
    public PriorityField(String pattern) {
      super(pattern);
    }
    public PriorityField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strPriority = append(data.strPriority, append, field);
      } else {
        data.strPriority = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PRI";
    }
  }

  /**
   * Channel field processor
   */
  public class ChannelField extends Field {

    private String append = null;

    public ChannelField() {};
    public ChannelField(String pattern) {
      super(pattern);
    }
    public ChannelField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, null);
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strChannel = append(data.strChannel, append, field);
      } else {
        data.strChannel = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CH";
    }
  }

  /**
   * GPS field processor
   */

  private static final Pattern LONG_GPS_PTN = Pattern.compile("\\b(\\d{1,3})(\\d{6})\\b");
  private int gpsFldCnt = 0;
  private String saveGPSLoc = "";
  public class GPSField extends Field {

    private int type;
    private boolean addDec = false;

    public GPSField() {
      this(0);
    };

    public GPSField(int type) {
      this.type = type;
    }

    public GPSField(String pattern) {
      this(0, pattern, false);
    }

    public GPSField(int type, String pattern) {
      this(type, pattern, false);
    }

    public GPSField(String pattern, boolean hardPattern) {
      this(0, pattern, hardPattern);
    }

    public GPSField(int type, String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
      this.type = type;
    }

    public GPSField(Pattern pattern) {
      this(0, pattern, false);
    }

    public GPSField(int type, Pattern pattern) {
      this(type, pattern, false);
    }

    public GPSField(Pattern pattern, boolean hardPattern) {
      this(0, pattern, hardPattern);
    }

    public GPSField(int type, Pattern pattern, boolean hardPattern) {
      super(pattern, hardPattern);
      this.type = type;
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      addDec = qual != null && qual.contains("d");
    }

    @Override
    public void parse(String field, Data data) {
      if (addDec) {
        field = LONG_GPS_PTN.matcher(field).replaceAll("$1.$2");
      }
      int tmp;
      if (type == 3) {
        tmp = ++gpsFldCnt;
        if (tmp > 2) return;
      } else {
        tmp = type;
      }
      switch (tmp) {
      case 1:
        saveGPSLoc = field;
        break;

      case 2:
        setGPSLoc(saveGPSLoc + ',' + field, data);
        saveGPSLoc = "";
        break;

      default:
        setGPSLoc(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "GPS";
    }
  }

  /**
   * Date field processor
   */
  public class DateField extends Field {

    private DateFormat fmt = null;
    private boolean hardPattern = false;
    private boolean convertDashes = false;

    public DateField() {};
    public DateField(String pattern) {
      super(pattern);
    }
    public DateField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }
    public DateField(DateFormat fmt) {
      this.fmt = fmt;
    }
    public DateField(DateFormat fmt, boolean hardPattern) {
      this.fmt = fmt;
      this.hardPattern = hardPattern;
    }
    public DateField(String pattern, DateFormat fmt, boolean hardPattern) {
      super(pattern, hardPattern);
      this.fmt = fmt;
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      convertDashes = qual != null && qual.contains("d");
    }

    @Override
    public boolean canFail() {
      return hardPattern || super.canFail();
    }

    @Override
    public void parse(String field, Data data) {
      if (fmt != null) {
        if (!checkParse(field, data) && hardPattern) abort();
      } else {
        if (convertDashes) field = field.replace('-', '/');
        data.strDate = field;
      }
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (fmt != null) {
        return setDate(fmt, field, data);
      } else {
        if (convertDashes) field = field.replace('-', '/');
        parse(field, data);
        return true;
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE";
    }
  }

  /**
   * Time field processor
   */
  public class TimeField extends Field {

    private DateFormat fmt = null;
    boolean hardPattern = false;

    public TimeField() {};
    public TimeField(String pattern) {
      super(pattern);
    }
    public TimeField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }
    public TimeField(DateFormat fmt) {
      this.fmt = fmt;
    }
    public TimeField(String pattern, DateFormat fmt, boolean hardPattern) {
      super(pattern, hardPattern);
      this.fmt = fmt;
    }
    public TimeField(DateFormat fmt, boolean hardPattern) {
      this.fmt = fmt;
      this.hardPattern = hardPattern;
    }

    @Override
    public boolean canFail() {
      return hardPattern || fmt != null || super.canFail();
    }

    @Override
    public void parse(String field, Data data) {
      if (fmt != null) {
        if (!checkParse(field, data) && hardPattern) abort();
      } else {
        data.strTime = field;
      }
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (fmt != null) {
        return setTime(fmt, field, data);
      } else {
        parse(field, data);
        return true;
      }
    }

    @Override
    public String getFieldNames() {
      return "TIME";
    }
 }

  /**
   * Date/Time field processor
   */
  public class DateTimeField extends Field {

    DateFormat fmt = null;
    boolean hardPattern = false;
    private boolean convertDashes = false;

    public DateTimeField () {};
    public DateTimeField (String pattern) {
      super(pattern);
    }
    public DateTimeField (String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    public DateTimeField(DateFormat fmt) {
      this.fmt = fmt;
    }

    public DateTimeField(DateFormat fmt, boolean hardPattern) {
      this.fmt = fmt;
      this.hardPattern = hardPattern;
    }

    public DateTimeField(String pattern, DateFormat fmt, boolean hardPattern) {
      super(pattern, hardPattern);
      this.fmt = fmt;
      this.hardPattern = hardPattern;
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      convertDashes = qual != null && qual.contains("d");
    }

    @Override
    public boolean canFail() {
      return hardPattern || super.canFail();
    }

    @Override
    public void parse(String field, Data data) {
      if (fmt != null) {
        if  (!checkParse(field, data) && hardPattern) abort();
      } else {
        int pt = field.indexOf(' ');
        if (pt >= 0) {
          data.strDate = field.substring(0,pt).trim();
          if (convertDashes) data.strDate = data.strDate.replace('-', '/');
          data.strTime = field.substring(pt+1).trim();
        }
      }
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (fmt != null) {
        return setDateTime(fmt, field, data);
      } else {
        int pt = field.indexOf(' ');
        if (pt < 0) return false;
        data.strDate = field.substring(0,pt).trim();
        if (convertDashes) data.strDate = data.strDate.replace('-', '/');
        data.strTime = field.substring(pt+1).trim();
        return true;
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME";
    }
  }

  /**
   * Time/Date field processor
   */
  public class TimeDateField extends Field {

    boolean convertDashes = false;

    public TimeDateField() {};
    public TimeDateField(String pattern) {
      super(pattern);
    }
    public TimeDateField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      convertDashes = qual != null && qual.contains("d");
    }

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      data.strTime = field.substring(0,pt).trim();
      String date = field.substring(pt+1).trim();
      if (convertDashes) date = date.replace('-', '/');
      data.strDate = date;
    }

    @Override
    public String getFieldNames() {
      return "TIME DATE";
    }
  }

  /**
   * Info URL field
   */
  public class InfoUrlField extends Field {
    public InfoUrlField(){}
    public InfoUrlField(String pattern) {
      super(pattern);
    }
    public InfoUrlField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("http://") && !field.startsWith("https://")) return false;
      parse(field, data);
      return true;
    }
    @Override
    public void parse(String field, Data data) {
      data.strInfoURL = field;
    }

    @Override
    public String getFieldNames() {
      return "URL";
    }
  }

  /**
   * Map field processor
   */
  public class AlertField extends Field {

    private String append = "-";

    public AlertField() {};
    public AlertField(String pattern) {
      super(pattern);
    }
    public AlertField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      append = buildConnector(qual, "-");
    }

    @Override
    public void parse(String field, Data data) {
      if (append != null) {
        data.strAlert = append(data.strAlert, append, field);
      } else {
        data.strAlert = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "ALERT";
    }
  }

  /**
   * Skip field processor
   */
  public class SkipField extends Field {

    boolean genAlert = false;
    boolean runReport = false;

    public SkipField() {};
    public SkipField(String pattern) {
      super(pattern);
    }
    public SkipField(String pattern, boolean hardPattern) {
      super(pattern, hardPattern);
    }

    @Override
    public void setQual(String qual) {
      if (qual != null) {
        genAlert = qual.contains("G");
        runReport = qual.contains("R");
      }
      super.setQual(qual);
    }
    @Override
    public void parse(String field, Data data) {
      if (runReport) data.msgType = MsgType.RUN_REPORT;
      else if (genAlert) data.msgType = MsgType.GEN_ALERT;
    }

    @Override
    public String getFieldNames() {
      return null;
    }
  }

  /**
   * Initials field processor
   * Field containing dispatcher initials, which is skipped
   * but has ability to verify initials contents
   */
  private static final Pattern INITLS_PAT = Pattern.compile("[A-Za-z]{2,3}");
  public class InitialsField extends SkipField {

    public InitialsField() {
      setPattern(INITLS_PAT);
    }

    @Override
    public void parse(String field, Data data) {
    }
  }

  /**
   * Empty field processor
   * Field which must be empty
   */
  public class EmptyField extends SkipField {

    boolean genAlert = false;
    boolean runReport = false;

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      if (qual != null) {
        genAlert = qual.contains("G");
        runReport = qual.contains("R");
      }
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.isEmpty()) return false;
      if (genAlert) data.msgType = MsgType.GEN_ALERT;
      if (runReport) data.msgType = MsgType.RUN_REPORT;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  /**
   * EndMark field processor
   * Does no field processing but can be tested.  Succeeds only if it is
   * the field immediately past the last data field
   */
  public class EndMarkField extends EndField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (isLastField(-1)) return false;
      return super.checkParse(field, data);
    }
  }

  /**
   * End field processor
   * Does no field processing but can be tested.  Succeeds only if it working
   * on a data a field beyond the limits of the field array
   */
  public class EndField extends SkipField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return isLastField(0) && !isLastField(-1);
    }

    @Override
    public void parse(String field, Data data) {
      if (!isLastField(0)) abort();
    }
  }

  public class SelectField extends SkipField {

    private String code;

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      this.code = qual;
    }

    @Override
    public String getQual() {
      return code;
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return code.equals(getSelectValue(data));
    }
  }

  public class EndTableField extends SkipField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean isHtmlTag() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals("<|\table|>");
    }
  }

  public class FailField extends Field {
    @Override
    public void parse(String field, Data data) {
      abort();
    }

    @Override
    public String getFieldNames() {
      return null;
    }
  }

  /**
   * Convert qual string to connection string
   * @param qual field qualifier
   * @param defConnector Default connector string
   * @return connection string
   */
  private String buildConnector(String qual, String defConnector) {

    if (qual ==  null) return defConnector;

    String result = null;
    for (char chr : qual.toCharArray()) {
      char delChar = (chr == 'D' ? '-' :
                      chr == 'N' ? '\n' :
                      chr == 'C' ? ',' :
                      chr == 'S' ? ' ' :
                      chr == 'L' ? '/' : 0);
      if (delChar != 0) {
        if (result == null) result = "";
        result += delChar;
      }
    }

    return (result != null ? result : defConnector);
  }

  private String select;

  protected String getSelectValue(Data data) {
    return getSelectValue();
  }

  protected void setSelectValue(String select) {
    this.select = select;
  }

  protected String getSelectValue() {
    return select;
  }

  /**
   * Look up the field processor associated with name
   * This should be overridden by classes that need some special field
   * processing
   * @param name requested field name
   * @return field processor
   */
  protected Field getField(String name) {

    if (name.equals("CALL")) return new CallField();
    if (name.equals("PLACE")) return new PlaceField();
    if (name.equals("ADDR")) return new AddressField();
    if (name.equals("CITY")) return new CityField();
    if (name.equals("ZIP")) return new ZipField();
    if (name.equals("ADDRCITY")) return new AddressCityField();
    if (name.equals("ADDRCITYST")) return new AddressCityStateField();
    if (name.equals("APT")) return new AptField();
    if (name.equals("APT2")) return new Apt2Field();
    if (name.equals("BLDG")) return new BuildingField();
    if (name.equals("X")) return new CrossField();
    if (name.equals("BOX")) return new BoxField();
    if (name.equals("UNIT")) return new UnitField();
    if (name.equals("ST")) return new StateField();
    if (name.equals("MAP")) return new MapField();
    if (name.equals("ID")) return new IdField();
    if (name.equals("PHONE")) return new PhoneField();
    if (name.equals("INFO")) return new InfoField();
    if (name.equals("UNK")) return new UnknownField();    // For unknown fields never known to be non-empty
    if (name.equals("LINFO")) return new LabelInfoField();
    if (name.equals("SRC")) return new SourceField();
    if (name.equals("CODE")) return new CodeField();
    if (name.equals("NAME")) return new NameField();
    if (name.equals("PLACENAME")) return new PlaceNameField();
    if (name.equals("PRI")) return new PriorityField();
    if (name.equals("CH")) return new ChannelField();
    if (name.equals("GPS")) return new GPSField();
    if (name.equals("GPS1")) return new GPSField(1);
    if (name.equals("GPS2")) return new GPSField(2);
    if (name.equals("GPS3")) return new GPSField(3);
    if (name.equals("DATE")) return new DateField();
    if (name.equals("TIME")) return new TimeField();
    if (name.equals("DATETIME")) return new DateTimeField();
    if (name.equals("TIMEDATE")) return new TimeDateField();
    if (name.equals("URL")) return new InfoUrlField();
    if (name.equals("ALERT")) return new AlertField();
    if (name.equals("SKIP")) return new SkipField();
    if (name.equals("INTLS")) return new InitialsField();
    if (name.equals("EMPTY")) return new EmptyField();
    if (name.equals("END")) return new EndField();
    if (name.equals("ENDMARK")) return new EndMarkField();
    if (name.equals("SELECT")) return new SelectField();
    if (name.equals("END_TABLE")) return new EndTableField();
    if (name.equals("FAIL")) return new FailField();

    throw new RuntimeException("Invalid field name: " + name);
  }

  @SuppressWarnings("serial")
  public static class FieldProgramException extends RuntimeException {};

  @Override
  public String toString() {

    if (startLink == null) return "Null Program";
    List<Step> stepList = getAllSteps();
    Map<Step,Integer> stepMap = new HashMap<Step,Integer>();
    int ndx = 1;
    for (Step step : stepList) stepMap.put(step, (ndx++));

    StringBuilder sb = new StringBuilder("Start:" + startLink.toString(stepMap));
    sb.append('\n');
    ndx = 1;
    for (Step step : stepList) {
      if (ndx > 1) sb.append('\n');
      sb.append(ndx++);
      sb.append(": ");
      sb.append(step.toString(stepMap));
    }
    return sb.toString();
  }
}
