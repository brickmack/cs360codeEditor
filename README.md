# cs360codeEditor

MAJOR CHANGES:

*All classes, methods, and attributes defined in the class diagram are included, though many are not yet functional. Removed/renamed existing ones as necessary to match current names

*Demo text files are now read in, not hardcoded (but address is hardcoded still)

*Removed most boilerplate code

*Added find/replace dialog box, but it has no functionality yet

*Language supports reading in some of its attributes from a configuration file. This is NOT to be used for actual highlighting yet (use the full constructor), only testing (but it looks to work pretty well so hopefully it can be fully implemented and used operationally soon)

*Added right click menu to the tab bar (currently does nothing, will add close all and other features later)

*Added line numbering (and eventually fold/unfold button) class


KNOWN BUGS:

*Redo does not work at all

*Undo is broken if any tab is closed

*Nested comments and/or strings behave incorrectly (string inside a comment shows as blue instead of green)


FURTHER WORK:

*File open dialog box (Aaron)

*File save and save as (Aaron)

*Add find/replace functionality (Austin)

*Fix issues with undo/redo (who?)

*Keyboard shortcuts (Aaron)

*Complete implementation of configuration file reader for Language, verify that it completely works, migrate to that instead of hardcoded Language objects (Mack)

*Detect variable declarations (Mack)

*Add code folding buttons (who?) and logic (who?)

*Create configuration file and test file for C (who?)
