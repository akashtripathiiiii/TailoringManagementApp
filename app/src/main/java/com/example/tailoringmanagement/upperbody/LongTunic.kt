package com.example.tailoringmanagement.upperbody

class LongTunic(
    frontLen: Float,
    backLen: Float,
    chest: Float,
    waist: Float,
    seat: Float,
    shoulderWidth: Float,
    armHole: Float,
    sleeveLen: Float,
    bicep: Float,
    cuff: Float,
    var strapLen: Float,
    var hemlineWidth: Float,
    var hemlineDesign: String
) : FullSleeve(frontLen, backLen, chest, waist, seat, shoulderWidth, armHole, sleeveLen, bicep, cuff)
