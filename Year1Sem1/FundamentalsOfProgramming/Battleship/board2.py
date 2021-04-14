from texttable import Texttable


class Board:
    def __init__(self):
        self._data = [0] * 36

    def set_square(self, row, col, value):
        self._data[row * 6 + col] = value

    def draw(self):
        board = Texttable()

        for i in range(7):
            row = []
            for j in range(7):

                if i == 0 and j == 0:
                    row.append(0)

                elif i == 0:
                    row.append(j)

                elif j == 0:
                    row.append(i)

                else:
                    row.append(self._data[6 * (i - 1) + j - 1])

            board.add_row(row)

        print(board.draw())

board = Board()
board.draw()