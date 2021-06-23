package net.anei.cadpage.parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Abstract combination parser that accepts the best results of a group of parsers
 */

public class GroupBestParser extends GroupBaseParser {

  // Lists of parsers are mutually compatible.  Which means that there
  // are some calls that they return identical results for.  Since we can
  // not tell which parser returned the results, we have to fudge the
  // default city/count and state in the final result
  private static final String[][] COMPAT_PARSER_LIST = new String[][]{
    {"NCDavidsonCountyA", "NCRowanCounty"}
  };

  private MsgParser[] parsers;

  private String sponsor;

  private Date sponsorDate;

  private SplitMsgOptions splitMsgOptions;

  public GroupBestParser(MsgParser ... parsersP) {

    // Build the final array of parsers.  eliminating parsers that are aliased
    // to another parser in the list
    List<MsgParser> parserList = new ArrayList<MsgParser>(parsersP.length);
    List<MsgParser> blockedParserList = new ArrayList<MsgParser>();
    Map<String, MsgParser> aliasMap = new HashMap<String, MsgParser>();

    // Add the list of parsers to our accumulated parser lists
    addParsers(parsersP, parserList, blockedParserList, aliasMap);

    // If we found any blocked parsers, they all get added to end of the regular
    // parser list behind a new GroupBlockParser
    if (!blockedParserList.isEmpty()) {
      parserList.add(new GroupBlockParser());
      parserList.addAll(blockedParserList);
    }

    // Convert the adjusted parser list back to an array
    parsers = parserList.toArray(new MsgParser[parserList.size()]);

    // Group parser is sponsored if all of it subparsers are sponsored
    // If all subparsers are sponsored, sponsor date is the earliest subparser sponsor date
    splitMsgOptions = null;
    sponsor = null;
    sponsorDate = null;
    for (MsgParser parser : parsers) {
      if (splitMsgOptions == null) splitMsgOptions = parser.getActive911SplitMsgOptions();
      String pSponsor = parser.getSponsor();
      if (pSponsor == null) {
        sponsor = null;
        sponsorDate = null;
        break;
      } else {
        Date pDate = parser.getSponsorDate();
        if (pDate == null) {
          if (sponsor == null) sponsor = pSponsor;
        } else {
          if (sponsorDate == null || sponsorDate.after(pDate)) {
            sponsor = pSponsor;
            sponsorDate = pDate;
          }
        }
      }
    }

    // Next we have to make adjustments when there are compatible parsers in the list
    for (String[] list : COMPAT_PARSER_LIST) {

      // First pass checking to see if we have 2 or more parsers from the compatible list
      int cnt = 0;
      String defCity = null;
      String defState = null;
      for (MsgParser parser : parsers) {
        String parserCode = parser.getParserCode();
        for (String code : list) {
          if (parserCode.equals(code)) {
            cnt++;
            defCity = merge(defCity, parser.getDefaultCity());
            defState = merge(defState, parser.getDefaultState());
            break;
          }
        }
      }

      // If we found 2 or more, make another pass replacing the affected
      // parser with an aliased parser that returns the appropriate defaults
      if (cnt >= 2) {
        for (int ndx = 0; ndx<parsers.length; ndx++) {
          MsgParser parser = parsers[ndx];
          String parserCode = parser.getParserCode();
          for (String code : list) {
            if (parserCode.equals(code)) {
              AliasedMsgParser aliasParser;
              if (parser instanceof AliasedMsgParser) {
                aliasParser = (AliasedMsgParser)parser;
              } else {
                parsers[ndx] = aliasParser = new AliasedMsgParser(parser);
              }
              aliasParser.setDefaults(defCity, defState);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Add list of parser to master parser list(s)
   * @param parsersP list of parsers to be added
   * @param parserList accumulated list of regular parsers
   * @param blockedParserList accumulated list of blocked parsers
   * @param aliasMap map of alias codes to parsers
   */
  private void addParsers(MsgParser[] parsersP, List<MsgParser> parserList, List<MsgParser> blockedParserList, Map<String, MsgParser> aliasMap) {

    // Run through the list of parsers
    for (MsgParser parser : parsersP) {

      // If we encounter a GropuBlockParsr, it does not get added, but we switch to
      // adding subsequent parsers to the blocked parser list
      if (parser instanceof GroupBlockParser) {
        parserList = blockedParserList;
      }

      // If parser is another GroupBestParser, call ourselves recursivelly to process it's parsers
      else if (parser instanceof GroupBestParser) {
        addParsers(((GroupBestParser)parser).parsers, parserList, blockedParserList, aliasMap);
      }

      // Otherwise just add the parser to the current parser list
      else {
        addParser(parser, parserList, aliasMap);
      }
    }

  }

  private void addParser(MsgParser parser, List<MsgParser> parserList,
      Map<String, MsgParser> aliasMap) {
    // Merge the default city/state and filter information.  None of these
    // are really used for any maping or filtering, but they do end up
    // calculating the displayed location name and sender filter.
    addParser(parser);

    // See if this parser has an alias code
    String aliasCode = parser.getAliasCode();
    if (aliasCode != null) {

      // Yep, see if we have already processed a parser with the same alias code
      MsgParser mainParser = aliasMap.get(aliasCode);
      if (mainParser != null) {

        // Yes again.  The main parser is going to replace the aliased parser
        // First step is to make sure the  main parser is an AliasedMsgParser
        // that we can adjust to include things that may differ between aliased
        // parsers
        AliasedMsgParser aliasParser;
        if (mainParser instanceof AliasedMsgParser) {
          aliasParser = (AliasedMsgParser)mainParser;
        } else {
          aliasParser = new AliasedMsgParser(mainParser);
          aliasMap.put(aliasCode, aliasParser);
          int ndx = parserList.indexOf(mainParser);
          parserList.set(ndx, aliasParser);
        }

        // Now that that is taken care of, just add the new parser
        // to the aliased one
        aliasParser.addMsgParser(parser);
        parser = null;
      }

      // No, we do not yet have another parser with this alias code.  So we keep this
      // parser, but add it to the alias map in case another parser is aliased to this one
      else {
        aliasMap.put(aliasCode, parser);
      }
    }

    // If we haven't dropped this parser because it is aliased to another one,
    // add it to this list
    if (parser != null) parserList.add(parser);
  }

  private String merge(String oldDefault, String newDefault) {
    if (oldDefault == null) return newDefault;
    if (oldDefault.equals(newDefault)) return oldDefault;
    return "";
  }

  @Override
  public void setTestMode(boolean testMode) {
    // Propogate the test mode status to all subparsers
    super.setTestMode(testMode);
    for (MsgParser parser : parsers) parser.setTestMode(testMode);
  }

  /**
   * @return list of component subparsers
   */
  MsgParser[] getParsers() {
    return parsers;
  }

  @Override
  protected Data parseMsg(Message msg, int parserFlags) {

    int bestScore = Integer.MIN_VALUE;
    Data bestData = null;

    for (MsgParser parser : parsers) {

      // If we encounter a GroupBlockParser in the list, see if
      // we have found anything so far.  If we have, return it
      if (parser instanceof GroupBlockParser) {
        if (bestData != null && bestData.msgType != MsgType.GEN_ALERT) return bestData;
      }

      // Otherwise invoke this parser and see what kind of result it returns.
      else {
        Data tmp = parser.parseMsg(msg, parserFlags);
        if (tmp != null) {
          // Score the result.
          // We want to seriously ding any result produced by any of the "General" parsers
          // including General Alert.  GENERAL ALERT type messages produced by location
          // parsers do not suffer this penalty
          int newScore = new MsgInfo(tmp).score();
          if (!tmp.parserCode.startsWith("General")) newScore += 100000;
          if (newScore > bestScore) {
            bestData = tmp;
            bestScore = newScore;
          }
        }
      }
    }

    return bestData;
  }

  // We have to override this to satisfy abstract requirements, but it will
  // never be called and doesn't have to do anything
  @Override
  protected boolean parseMsg(String strMessage, Data data) {
    return false;
  }

  @Override
  public String getSponsor() {
    return sponsor;
  }

  @Override
  public Date getSponsorDate() {
    return sponsorDate;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return splitMsgOptions;
  }
}
