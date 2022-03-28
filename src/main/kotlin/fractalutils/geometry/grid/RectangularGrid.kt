package fractalutils.geometry.grid

import fractalutils.checking.Check
import fractalutils.geometry.double2.Double2
import fractalutils.geometry.int2.Int2
import fractalutils.geometry.int2.MutableInt2
import fractalutils.geometry.rectangle.MutableRect
import fractalutils.geometry.rectangle.Rect
import fractalutils.math.fastFloor

/**
 * Represents some kind of regular division of the 2D plane,
 * in this case a regular rectangular division.
 */
// TODO: If we get more grids, extract a superinterface.
class RectangularGrid(
    cellSizeX: Double = 1.0,
    cellSizeY: Double = cellSizeX,
    var offsetX: Double = 0.0,
    var offsetY: Double = 0.0

) {
    /**
     * Creates a new grid, using the specified are as the cell for grid cell 0,0.
     */
    constructor(origoCellArea: Rect): this(origoCellArea.width, origoCellArea.height, origoCellArea.minX, origoCellArea.minY)

    var cellSizeX: Double = cellSizeX
        set(value) {
            Check.greater(value, "cellSizeX", 0.0)
            field = value
        }
    var cellSizeY: Double = cellSizeY
        set(value) {
            Check.greater(value, "cellSizeY", 0.0)
            field = value
        }

    fun getCellXPosFromPos(posX: Double): Double = ((posX - offsetX) / cellSizeX).fastFloor() * cellSizeX
    fun getCellYPosFromPos(posY: Double): Double = ((posY - offsetY) / cellSizeY).fastFloor() * cellSizeY

    fun getCellAreaFromPos(pos: Double2, areaOut: MutableRect = MutableRect()): MutableRect = getCellAreaFromPos(pos.x, pos.y, areaOut)
    fun getCellAreaFromPos(posX: Double, posY: Double, areaOut: MutableRect = MutableRect()): MutableRect {
        areaOut.set(
            getCellXPosFromPos(posX),
            getCellYPosFromPos(posY),
            cellSizeX,
            cellSizeY
        )
        return areaOut
    }

    fun getCellXPosFromCell(cellX: Int): Double = offsetX + cellX * cellSizeX
    fun getCellYPosFromCell(cellY: Int): Double = offsetY + cellY * cellSizeY

    fun getCellAreaFromCell(cell: Int2, areaOut: MutableRect = MutableRect()): MutableRect = getCellAreaFromCell(cell.x, cell.y, areaOut)
    fun getCellAreaFromCell(cellX: Int, cellY: Int, areaOut: MutableRect = MutableRect()): MutableRect {
        areaOut.set(
            getCellXPosFromCell(cellX),
            getCellYPosFromCell(cellY),
            cellSizeX,
            cellSizeY
        )
        return areaOut
    }

    fun getCellXFromPos(xPos: Double): Int = ((xPos - offsetX) / cellSizeX).fastFloor()
    fun getCellYFromPos(yPos: Double): Int = ((yPos - offsetY) / cellSizeY).fastFloor()

    fun getCellFromPos(pos: Double2, cellOut: MutableInt2 = MutableInt2()): MutableInt2 = getCellFromPos(pos.x, pos.y, cellOut)
    fun getCellFromPos(xPos: Double, yPos: Double, cellOut: MutableInt2 = MutableInt2()): MutableInt2 {
        cellOut.set(getCellXFromPos(xPos),
            getCellYFromPos(yPos))
        return cellOut
    }

    /**
     * Same as visitIntersecting, but passes the point and area as separate parameters to the visitor.
     */
    inline fun visitIntersectingExploded(area: Rect,
                                         visitor: (cellX: Int,
                                                   cellY: Int,
                                                   minX: Double,
                                                   minY: Double,
                                                   width: Double,
                                                   height: Double) -> Boolean): Boolean {

        Check.greater(cellSizeX, "cellSizeX", 0.0)
        Check.greater(cellSizeY, "cellSizeY", 0.0)

        if (area.empty) return true

        val cellX1 = getCellXFromPos(area.minX)
        val cellX2 = getCellXFromPos(area.maxX)
        val cellY1 = getCellYFromPos(area.minY)
        val cellY2 = getCellYFromPos(area.maxY)

        for (cellY in cellY1 .. cellY2)
            for (cellX in cellX1 .. cellX2) {
                val x1 = offsetX + cellX * cellSizeX
                val y1 = offsetY + cellY * cellSizeY

                if (!visitor(cellX, cellY, x1, y1, cellSizeX, cellSizeY)) return false
            }

        return true
    }

    /**
     * @param area area where all intersecting grids should be visited.
     * @param visitor called for each grid intersecting or touching the given area.
     *        NOTE: The cell and cellArea parameters are re-used to avoid excessive object creation,
     *        no references to them should be saved!  Create copies of them if you want to store them somewhere.
     * @return true if all visited, false if visitor aborted.
     */
    inline fun visitIntersecting(area: Rect,
                                 visitor: (cell: Int2, cellArea: Rect) -> Boolean): Boolean {

        Check.greater(cellSizeX, "cellSizeX", 0.0)
        Check.greater(cellSizeY, "cellSizeY", 0.0)

        if (area.empty) return true

        val cellX1 = getCellXFromPos(area.minX)
        val cellX2 = getCellXFromPos(area.maxX)
        val cellY1 = getCellYFromPos(area.minY)
        val cellY2 = getCellYFromPos(area.maxY)

        val cell = MutableInt2()
        val cellArea = MutableRect()

        for (cellY in cellY1 .. cellY2)
            for (cellX in cellX1 .. cellX2) {
                cell.set(cellX, cellY)
                getCellAreaFromCell(cell, cellArea)

                if (!visitor(cell, cellArea)) return false
            }

        return true
    }

}