'''
Created on Jan 4, 2020

@author: dianatibre
'''
import unittest
from Validations import Validations, InputError
from Board import Board


class TestBoard(unittest.TestCase):


    def testGetWon_NewBoard_False(self):
        board=Board()
        self.assertEqual(board.get_won(), False)
        
    def testGetWon_GameFinished_True(self):
        board=Board()
        board.set_won(True)
        self.assertEqual(board.get_won(), True)
        
    def testGetWinner_GameFinished_Winner(self):
        board=Board()
        board.set_winner('X')
        self.assertEqual(board.get_winner(), 'X')
        
    def testIfWon_GameFinished_Winner(self):
        board=Board()
        board.set_board(5, 1, 'X')
        board.set_board(5, 2, 'X')
        board.set_board(5, 3, 'X')
        board.set_board(5, 4, 'X')
        board.if_won()
        self.assertEqual(board.get_winner(), 'X')
        
    def testIfWon_GameNotFinished_NotWon(self):
        board=Board()
        board.set_board(5, 1, 'X')
        board.set_board(5, 2, '0')
        board.set_board(5, 3, 'X')
        board.set_board(5, 4, 'X')
        board.if_won()
        self.assertEqual(board.get_won(), False)
        
    def testIfWon_GameFinished_Won(self):
        board=Board()
        board.set_board(5, 1, 'X')
        board.set_board(5, 2, 'X')
        board.set_board(5, 3, 'X')
        board.set_board(5, 4, 'X')
        board.if_won()
        self.assertEqual(board.get_won(), True)
    
    def testChooseColumn_BlankBoard_FirstColumn(self):
        board=Board()
        self.assertEqual(board.choose_column(), 0)


class TestValidations(unittest.TestCase):
    
    def testColumnValidation_InvalidInput_ErrorMessage(self):
        test_validations=Validations()
        column=8
        try:
            test_validations.ColumnValidation(column)
            assert(False)
        except InputError as error:
            self.assertEqual(str(error),'Column out of range!')

    def testColumnValidation_InvalidInput_ErrorMessageException(self):
        test_validations=Validations()
        column=8
        with self.assertRaises(InputError) as error:
            test_validations.ColumnValidation(column)
        self.assertEqual('Column out of range!', str(error.exception))
    
        

if __name__ == "__main__":
    unittest.main()
    