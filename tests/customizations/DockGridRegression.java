package customizations;

import app.lawnchair.hotseat.DockGrid;
import com.android.launcher3.celllayout.CellPosMapper;
import com.android.launcher3.celllayout.CellPosMapper.CellPos;
import com.android.launcher3.celllayout.CellPosMapper.TwoPanelCellPosMapper;

/** JVM regression test for the production dock mapping; no Android runtime needed. */
public final class DockGridRegression {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        int cases = 0;
        for (int columns = 3; columns <= 10; columns++) {
            for (int rows = 1; rows <= 2; rows++) {
                for (boolean vertical : new boolean[] {false, true}) {
                    CellPosMapper mapper = new CellPosMapper(vertical, columns);
                    boolean[][] occupied = new boolean[vertical ? rows : columns][vertical ? columns : rows];
                    for (int rank = 0; rank < columns * rows; rank++) {
                        int x = DockGrid.cellX(rank, columns, vertical);
                        int y = DockGrid.cellY(rank, columns, vertical);
                        check(!occupied[x][y], "Two ranks mapped to the same dock cell");
                        occupied[x][y] = true;
                        CellPos saved = mapper.mapPresenterToModel(x, y, 999, -101);
                        check(saved.screenId == rank, "Saving a rotated/second-row icon changed its rank");
                        cases++;
                    }
                }
            }
        }
        // Concrete boundaries: second row starts with rank 4; the bottom icon is
        // rank 0 in a vertical dock, with rank 4 in the adjacent column.
        check(DockGrid.cellX(4, 4, false) == 0 && DockGrid.cellY(4, 4, false) == 1, "Second row start");
        check(DockGrid.cellX(4, 4, true) == 1 && DockGrid.cellY(4, 4, true) == 3, "Rotated second row");
        CellPosMapper foldable = new TwoPanelCellPosMapper(5, 8);
        check(foldable.mapPresenterToModel(7, 1, 0, -101).screenId == 15, "Unfolded dock rank");
        check(foldable.mapPresenterToModel(7, 2, 0, -100).equals(new CellPos(2, 2, 1)), "Desktop panel mapping changed");
        check(CellPosMapper.DEFAULT.mapPresenterToModel(3, 0, 8, -101).screenId == 3, "Default mapper changed");
        System.out.println("PASS: " + cases + " dock round trips plus boundary, foldable and desktop checks");
    }
}
