    ; Usage of these functions are pretty self-explanatory, but take a look at the test program at the
    ; bottom to see the functions in action.
    ;
    ; Basically it's setting the gradient dimensions, adding the line colors and your gradient is ready
    ; for display.

     ;****************************************************************************************************
     ;**
     ;**     Gradient
     ;**
     ;****************************************************************************************************
     
     Type Gradient
          Field OffsetX
          Field OffsetY
          Field SizeX
          Field SizeY
          Field Count
     End Type
     
     Function Gradient_Create.Gradient ()
          Gradient_Destroy
          Gradient.Gradient = New Gradient
          Gradient\OffsetX = 0
          Gradient\OffsetY = 0
          Gradient\SizeX = 0
          Gradient\SizeY = 0
          Gradient\Count = 0
          Return Gradient
     End Function
     
     Function Gradient_Destroy ()
          Delete Each Gradient
          Delete Each Gradient_Color
     End Function
     
     Function Gradient_Set ( PosX , PosY , SizeX , SizeY )
          Gradient.Gradient = First Gradient
          Gradient\OffsetX = PosX
          Gradient\OffsetY = PosY
          Gradient\SizeX = SizeX
          Gradient\SizeY = SizeY
     End Function
     
     ;= Essential initialization
     Gradient_Create
     
     ;****************************************************************************************************
     ;**
     ;**     Color gradient
     ;**
     ;****************************************************************************************************
     
     Const Color_Red = 0
     Const Color_Green = 1
     Const Color_Blue = 2
     
     Type Gradient_Color
          Field Color[ 2 ]
     End Type
     
     Function Gradient_Clear ()
          Delete Each Gradient_Color
          Gradient.Gradient = First Gradient
          Gradient\Count = 0
     End Function
     
     Function Gradient_Add ( Red , Green , Blue )
          Color.Gradient_Color = New Gradient_Color
          Color\Color[ Color_Red ] = Red
          Color\Color[ Color_Green ] = Green
          Color\Color[ Color_Blue ] = Blue
          Gradient.Gradient = First Gradient
          Gradient\Count = Gradient\Count + 1
     End Function
     
     Function Gradient_Display ()
          Gradient.Gradient = First Gradient
          Gradient_Color_Range# = Gradient\SizeY / ( Gradient\Count - 1 )
          Local PrevColor.Gradient_Color
          ThisColor.Gradient_Color = First Gradient_Color
          Local Color_Min[ 2 ]
          Local Color_Max[ 2 ]
          Local Color_Reverse[ 2 ]
          Local Color_Final[ 2 ]
          Repeat
               ;= Next color
               PrevColor = ThisColor
               ThisColor = After ThisColor
               Gradient_Color_Number = Gradient_Color_Number + 1
               ;= Convert gradient color number to pixel offset for each color
               Gradient_Color_Offset = Gradient\SizeY * ( Gradient_Color_Number - 1 ) / ( Gradient\Count - 1 )
               ;= Red, green, blue
               For Color = 0 To 2
                    If PrevColor\Color[ Color ] > ThisColor\Color[ Color ]
                         Color_Min[ Color ] = ThisColor\Color[ Color ]
                         Color_Max[ Color ] = PrevColor\Color[ Color ]
                         Color_Reverse[ Color ] = True
                    Else
                         Color_Min[ Color ] = PrevColor\Color[ Color ]
                         Color_Max[ Color ] = ThisColor\Color[ Color ]
                         Color_Reverse[ Color ] = False
                    End If
               Next
               ;= Loop through all pixel lines of this color gradient
               For Gradient_Line = 0 To Gradient_Color_Range
                    ;= Loop through individual color components
                    For Color = 0 To 2
                         ;= Zero-based range
                         Color_Range = Color_Max[ Color ] - Color_Min[ Color ]
                         ;= Convert pixel range of this gradient section to color range
                         Gradient_Base_Value = Gradient_Line * Color_Range / Gradient_Color_Range
                         ;= Reverse base value if color value is lower
                         If Color_Reverse[ Color ]
                              Gradient_Value = Color_Range - Gradient_Base_Value
                         ;= Otherwise keep default base value
                         Else
                              Gradient_Value = Gradient_Base_Value
                         End If
                         ;= Actual gradient color value
                         Color_Value = Gradient_Value + Color_Min[ Color ]
                         Color_Final[ Color ] = Color_Value
                    Next
                    ;= Render this line
                    Color Color_Final[ Color_Red ] , Color_Final[ Color_Green ] , Color_Final[ Color_Blue ]
                    X_Left = Gradient\OffsetX
                    X_Right = X_Left + Gradient\SizeX
                    Y_Top = Gradient\OffsetY + Gradient_Color_Offset + Gradient_Line
                    Y_Bottom = Y_Top
                    Line X_Left , Y_Top , X_Right , Y_Bottom
               Next
          Until After ThisColor = Null
     End Function
     
;** BREAK **
     
     ;= Test program
     
     Graphics 640 , 480 , 32,2
     
     Gradient_Set 0 , 0 , 640 , 480
     Gradient_Add 0 , 0 , 0
     Gradient_Add 213 , 192 , 112
     Gradient_Add 128 , 0 , 59
     Gradient_Add 0 , 0 , 95
     Gradient_Add 0 , 0 , 0
     Gradient_Display
     
     WaitKey
     End
     
;-->
;~IDEal Editor Parameters:
;~C#Blitz3D