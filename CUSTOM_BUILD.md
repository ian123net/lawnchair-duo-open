# Lawnchair 15 Custom

Based on Lawnchair 15 Beta 3 (`15-dev`, upstream commit
`505dbc40e6154c05158b5d0271c45f6a885a411b`).

## New settings

- **Settings → Dock → Grid → Dock rows:** choose one or two rows. The existing
  Dock icons setting controls the number of icons **per row**. In a vertical
  landscape dock, two rows become two columns.
- **Settings → Home screen → Icons → Two-line app names:** wrap long app names
  onto up to two lines on the home screen and in the dock. Enable **Show labels**
  in Dock settings to see dock names. Names longer than two lines still end in
  an ellipsis. Drawer and folder label settings are unchanged.

Both settings retain the original defaults (one dock row, one label line).
Reducing dock capacity moves overflow apps and folders onto new home-screen
pages. This preserves shortcuts and folder contents instead of discarding them.
The dock and workspace reserve space for the additional rows and text.

## Install or build

The GitHub debug variant installs as **Lawnchair 15 Custom**, package
`app.lawnchair.tworow`, alongside the official launcher. It does not automatically
inherit the other app's data. To copy a layout, create a backup in your existing
Lawnchair and restore it in this app, then enable the two settings above.

Build with JDK 21, Android SDK platform 36.1 and Build Tools 36.1.0:

```sh
git submodule update --init --recursive
./gradlew assembleLawnWithQuickstepGithubDebug
./gradlew spotlessCheck
```

The APK appears in `build/outputs/apk/lawnWithQuickstepGithub/debug/`.
On a GitHub fork, the **Build Lawnchair 15 Custom** workflow runs when this
branch is pushed and uploads the APK as an Actions artifact. These are
development builds signed with the build machine's debug key, not official
Lawnchair releases.

## Validation

Run the SDK-independent dock persistence regression suite with a JDK on PATH:

```sh
python3 tests/customizations/run_regressions.py
```

It compiles the production dock mapper and checks all ranks for 3–10 icons per
row, one/two rows, horizontal/vertical orientation, foldable panels, and the
unchanged desktop mapping. Only the Android model constants and fields are
stubbed, not the mapping logic.

Android test additions cover two-line content height and dock overflow migration
in the existing `tests/multivalentTests` suite. Those tests require the upstream
Android test environment; the standalone Java regression suite does not exercise
Android views or SQLite.

Before treating a build as a daily-use launcher, verify on a device:

1. Enable two dock rows, fill both rows, restart, and rotate the device.
2. Move icons between dock rows and the home screen; create and reopen a folder.
3. Reduce the dock to one row and check that overflow shortcuts/folders appear
   on new home-screen pages.
4. Enable two-line names and check short, long, unbroken, and non-Latin names at
   your preferred icon size, label size, grid density, and Android font scale.
5. Toggle dock labels and the dock search bar; on a foldable, fold and unfold.
6. Disable both new settings and verify the ordinary one-row/one-line layout.

## Implementation notes

The dock backport follows the approach in upstream PR #6979 by MrSluffy, with
additional handling for vertical docks, two-panel rank mapping, database grid
identity, row spacing, and overflow preservation. Two-line labels use Android's
native text wrapping and retain end ellipsis, including keyboard focus.
Original Lawnchair and AOSP license notices remain in place.
