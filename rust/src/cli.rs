use clap::{ArgGroup, Parser, ValueEnum};

use crate::grids;
use crate::solver;
use crate::solver::Solver;

use crate::backtrack::SolverBtrack;
use crate::proceduralv1::SolverProceV1;
use crate::proceduralv2::SolverProceV2;
use crate::proceduralv3::SolverProceV3;
use crate::proceduralv4::SolverProceV4;
use crate::proceduralv5::SolverProceV5;
use crate::proceduralv6::SolverProceV6;
use crate::proceduralv7::SolverProceV7;
use crate::proceduralv8::SolverProceV8;

#[derive(Parser, Debug)]
#[command(name = "sudoku_solver")]
#[command(about = "Sudoku solvers from 3 to 5 grid sizes")]
#[command(group(ArgGroup::new("sudoku").required(true).args(["g", "grid"])))]
pub struct Cli {
    #[arg(short, value_name = "GRID_NAME", group = "sudoku")]
    pub g: Option<PresetGrids>,

    #[arg(long, value_name = "GRID", group = "sudoku")]
    pub grid: Option<String>,

    #[arg(
        short,
        long,
        default_value_t = true,
        help = "Does the alphabet contain numbers or just letters"
    )]
    pub alnum: bool,

    #[arg(
        long,
        default_value_t = false,
        help = "Removes the display of both pre solving and post solving grids"
    )]
    pub no_print: bool,

    #[arg(short, long, value_enum, value_delimiter=',',default_values_t = [Solvers::Last])]
    pub solvers: Vec<Solvers>,
}

#[derive(ValueEnum, Clone, Debug)]
pub enum PresetGrids {
    Grid2Empty,
    Grid2NumEasy,
    Grid2NumA,
    Grid2NumB,
    Grid2NumC,
    Grid3Empty,
    Grid3NumA,
    Grid3NumHard,
    Grid4NumEmpty,
    Grid4NumA,
    Grid4NumC,
    Grid5NumEmpty,
    Grid5NumEasy,
    Grid5NumAdvanced,
    Grid5AlphaEasy,
    Grid5AlphaAdvanced,
}

impl PresetGrids {
    pub fn as_str(&self) -> String {
        match self {
            PresetGrids::Grid2Empty => grids::GRID_2_EMPTY.to_string(),
            PresetGrids::Grid2NumEasy => grids::GRID_2_NUM_EASY.to_string(),
            PresetGrids::Grid2NumA => grids::GRID_2_NUM_A.to_string(),
            PresetGrids::Grid2NumB => grids::GRID_2_NUM_B.to_string(),
            PresetGrids::Grid2NumC => grids::GRID_2_NUM_C.to_string(),
            PresetGrids::Grid3Empty => grids::GRID_3_EMPTY.to_string(),
            PresetGrids::Grid3NumA => grids::GRID_3_NUM_A.to_string(),
            PresetGrids::Grid3NumHard => grids::GRID_3_NUM_HARD.to_string(),
            PresetGrids::Grid4NumEmpty => grids::GRID_4_EMPTY.to_string(),
            PresetGrids::Grid4NumA => grids::GRID_4_NUM_A.to_string(),
            PresetGrids::Grid4NumC => grids::GRID_4_NUM_C.to_string(),
            PresetGrids::Grid5NumEmpty => grids::GRID_5_EMPTY.to_string(),
            PresetGrids::Grid5NumEasy => grids::GRID_5_NUM_EASY.to_string(),
            PresetGrids::Grid5NumAdvanced => grids::GRID_5_NUM_ADVANCED.to_string(),
            PresetGrids::Grid5AlphaEasy => grids::GRID_5_ALPHA_EASY.to_string(),
            PresetGrids::Grid5AlphaAdvanced => grids::GRID_5_ALPHA_ADVANCED.to_string(),
        }
    }
}

#[derive(ValueEnum, Clone, Debug, PartialEq)]
pub enum Solvers {
    Backtrack,
    ProceV1,
    ProceV2,
    ProceV3,
    ProceV4,
    ProceV5,
    ProceV6,
    ProceV7,
    ProceV8,
    Last,
}

impl Solvers {
    pub fn resolve(self) -> Self {
        match self {
            Self::Last => Self::ProceV8,
            other => other,
        }
    }

    pub fn builder(&self) -> Box<dyn Fn(&String) -> Box<dyn solver::Solver>> {
        match self {
            Self::Backtrack => Box::new(|grid: &String| Box::new(SolverBtrack::new(grid))),
            Self::ProceV1 => Box::new(|grid: &String| Box::new(SolverProceV1::new(grid))),
            Self::ProceV2 => Box::new(|grid: &String| Box::new(SolverProceV2::new(grid))),
            Self::ProceV3 => Box::new(|grid: &String| Box::new(SolverProceV3::new(grid))),
            Self::ProceV4 => Box::new(|grid: &String| Box::new(SolverProceV4::new(grid))),
            Self::ProceV5 => Box::new(|grid: &String| Box::new(SolverProceV5::new(grid))),
            Self::ProceV6 => Box::new(|grid: &String| Box::new(SolverProceV6::new(grid))),
            Self::ProceV7 => Box::new(|grid: &String| Box::new(SolverProceV7::new(grid))),
            Self::ProceV8 => Box::new(|grid: &String| Box::new(SolverProceV8::new(grid))),
            Self::Last => Self::ProceV8.builder(),
        }
    }
}
