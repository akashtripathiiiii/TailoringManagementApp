package com.example.tailoringmanagement.upperbody

class HalfSleeveShirt(
    frontLen: Float,
    backLen: Float,
    chest: Float,
    waist: Float,
    seat: Float,
    shoulderWidth: Float,
    armHole: Float,
    var shirtBottomDesign: String,
    var strapDes: String
) : UpperBody(frontLen, backLen, chest, waist, seat, shoulderWidth, armHole)
