from PIL import Image

def ritaglia_immagine(input_path, output_path, coordinates):
    """
    Ritaglia un'immagine data le coordinate del rettangolo.

    Args:
    - input_path: Percorso dell'immagine originale.
    - output_path: Percorso dell'immagine ritagliata di output.
    - coordinates: Una tupla di coordinate (x0, y0, x1, y1) per il rettangolo da ritagliare.
    """
    with Image.open(input_path) as img:
        # Ritaglia l'immagine usando le coordinate fornite
        img_cropped = img.crop(coordinates)
        # Salva l'immagine ritagliata
        img_cropped.save(output_path)
        print(f"Immagine salvata: {output_path}")

# Esempio di uso
input_path = 'prova.png'
output_path = 'prova_ritagliata_4.png'
coordinates = (38, 38, 574, 408)  # Sostituisci con le tue coordinate (x0, y0, x1, y1)

ritaglia_immagine(input_path, output_path, coordinates)